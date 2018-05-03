import sys
import logging
import rds_config
import pymysql
import json
import datetime
from rds_config import DateTimeEncoder

rds_host  = rds_config.db_endpoint
name = rds_config.db_username
password = rds_config.db_password
db_name = rds_config.db_name
port = rds_config.db_port

logger = logging.getLogger()
logger.setLevel(logging.INFO)


def check_stock(cur, products):

    warehouse = 0
    remainingStock = dict()
    for product in products:
        cur.execute('select * from Warehouse_has_Product where Product_idProduct = "'
                    + product['Product_idProduct']+ '"' + ' AND Size_code = "' +
                    product['productSize'] + '"')
        
        stock = 0
        for row in cur:
            warehouse = row['Warehouse_idWarehouse']
            stock = max(stock, row['stock'])

        if product['quantity'] > stock:
            return -1, product

        remainingStock[product['Product_idProduct']] = stock - product['quantity']

    return warehouse, remainingStock
        

def lambda_handler(event, context):
    """
    This function inserts a Sale or obtains the Sales from a Customer.
    """

    try:
        conn = pymysql.connect(rds_host, user=name,
                               passwd=password, db=db_name, connect_timeout=5)
    except Exception as e:
        logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
        logger.error(e)
        sys.exit()

    logger.info("SUCCESS: Connection to RDS mysql instance succeeded")

    remainingStock = None
    warehouse = 0

    with conn.cursor(pymysql.cursors.DictCursor) as cur:
        if event['httpMethod'] == 'POST':
            sale = json.loads(event['body'])
            bundle = sale['bundle']            

            warehouse, remainingStock = check_stock(cur, bundle['products'])
            
            if warehouse == -1:
                message = { 'message': 'Not enough stock available for ' + remainingStock['Product_idProduct'] }
                return {
                    'statusCode': 400,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': json.dumps(message, cls=DateTimeEncoder, encoding='latin1')
                }

            sale['warehouse'] = warehouse

            if 'promotion' in sale:
                promotion = sale['promotion']
                query = 'update Promotions set status = '+str(promotion['status'])
                query += ' where code = "' + promotion['code'] + '"'
                cur.execute(query)
                
            query = 'insert into Bundle (name, frequencyDays, description, '
            query += 'lastOrdered, nextDelivery, preferredHour, '
            query += 'Customer_idCustomer) values("'
            query += bundle['name']+'", '+str(bundle['frequencyDays'])
            lastOrdered = datetime.datetime.strptime(bundle['lastOrdered'], "%d/%m/%Y")
            nextDelivery = datetime.datetime.strptime(bundle['nextDelivery'], "%d/%m/%Y")
            preferredHour = datetime.datetime.strptime(bundle['preferredHour'], "%d/%m/%Y %H:%M")
            query += ', "'+bundle['description']+'", "'+lastOrdered.strftime('%Y-%m-%d')
            query += '", "'+nextDelivery.strftime('%Y-%m-%d')
            query += '", "'+preferredHour.strftime('%Y-%m-%d %H:%M')
            query += '", "'+str(bundle['Customer_idCustomer'])+'")'

            cur.execute(query)
            idBundle = cur.lastrowid

            for product in bundle['products']:
                address = product['address']

                if 'latitude' in address:
                    pass
                else:
                    address['latitude'] = 'NULL'

                if 'longitude' in address:
                    pass
                else:
                    address['longitude'] = 'NULL'
            
                query = 'insert into Address (addressName, addressNumber, '
                query += 'latitude, longitude, district, city) values("'
                query += address['addressName']
                query += '", "'+address['addressNumber']+'", '+str(address['latitude'])
                query += ', '+str(address['longitude'])+', "'+address['district']
                query += '", "'+address['city']+'")'
                cur.execute(query)

                idAddress = cur.lastrowid
            
                query = 'insert into Product_has_Bundle (Product_idProduct, Bundle_idBundle, '
                query += 'quantity, dateDefault, dateOrder, Address_idAddress, '
                query += 'Subtotal, productSize) values("'
                query += product['Product_idProduct']+'", '+str(idBundle)
                dateDefault = datetime.datetime.strptime(product['dateDefault'], "%d/%m/%Y")
                dateOrder = datetime.datetime.strptime(product['dateOrder'], "%d/%m/%Y")
                query += ', '+str(product['quantity'])+', "'+dateDefault.strftime('%Y-%m-%d')
                query += '", "'+dateOrder.strftime('%Y-%m-%d')
                query += '", '+str(idAddress)
                query += ', '+str(product['Subtotal'])+', "'+product['productSize']+'")'
                logger.info(query)
                cur.execute(query)

                newStock = remainingStock[product['Product_idProduct']]

                query = 'UPDATE Warehouse_has_Product SET stock = ' + str(newStock)
                query += ' WHERE Warehouse_idWarehouse = '+ str(warehouse)
                query += ' and Product_idProduct = "' + product['Product_idProduct']
                query += '" and Size_code = "' + product['productSize'] + '"'
                logger.info(query)
                cur.execute(query)
            
            query = 'insert into Sale (total, rating, status, '
            query += 'Bundle_idBundle, Bundle_Customer_idCustomer, '
            query += 'typeSale_idtypeSale, Evener_idEvener, typePayment, '
            query += 'amountToPay, warehouse, promotionCode) values('
            query += str(sale['total'])+', '
            if 'rating' in sale:
                if sale['rating'] is None:
                    query += 'NULL'
                else:
                    query += str(sale['rating'])
            else:
                query += 'NULL'
            query += ', '+str(sale['status'])+', '+str(idBundle)
            query += ', "'+str(sale['Bundle_Customer_idCustomer'])
            query += '", '+str(sale['typeSale_idtypeSale'])
            query += ', '+str(sale['Evener_idEvener'])
            query += ', ' + str(sale['typePayment'])
            query += ', ' + str(sale['amountToPay'])
            query += ', ' + str(sale['warehouse']) + ', '
            if 'promotion' in sale:
                if sale['promotion'] is None:
                    query += 'NULL'
                else:
                    query += '"' + sale['promotion']['code'] + '"'
            else:
                query += 'NULL'
            query += ')'
            cur.execute(query)
            conn.commit()
            conn.close()

            return {
                'statusCode': 200,
                'headers': { 'Content-Type': 'application/json' },
                'body': event['body']
            }
            
        elif event['httpMethod'] == 'GET':
            if event['pathParameters'] is not None:
                idCustomer = event['pathParameters']['idCustomer']
                idCustomer = idCustomer.replace("_",":")
                cur.execute('select * from Sale where Bundle_Customer_idCustomer = "' + idCustomer + '"')
                sales = []
                for row in cur:
                    sales.append(row)

                bundles = {}
                cur.execute('select * from Bundle where Customer_idCustomer = "' + idCustomer + '"')
                for row in cur:
                    bundles[row['idBundle']] = row

                for sale in sales:
                    sale['bundle'] = bundles[sale['Bundle_idBundle']]
                    if 'promotionCode' in sale:
                        if not sale['promotionCode'] is None:
                            logger.info('select * from Promotions where code = "' + str(sale['promotionCode']) + '"')
                            cur.execute('select * from Promotions where code = "' + str(sale['promotionCode']) + '"')
                            for row in cur:
                                sale['promotion'] = row
                        del sale['promotionCode']

                for sale in sales:
                    cur.execute('select * from Product_has_Bundle where Bundle_idBundle = ' + str(sale['Bundle_idBundle']))
                    products = []
                    for row in cur:
                        products.append(row)

                    for product in products:
                        cur.execute('select * from Address where idAddress = ' + str(product['Address_idAddress']))
                        for row in cur:
                            product['address'] = row
                        del product['Address_idAddress']
                    sale['bundle']['products'] = products
                    del sale['Bundle_idBundle']

                conn.close()
                return {
                    'statusCode': 200,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': json.dumps(sales, cls=DateTimeEncoder, encoding='latin1')
                }
            else:
                conn.close()
                return {
                    'statusCode': 404,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': 'not found.'
                }
        elif event['httpMethod'] == 'PATCH':
            sale = json.loads(event['body'])
            bundle = sale['bundle']
            query = 'select status from Sale where idSale = ' + str(sale['idSale'])
            cur.execute(query)
            currentStatus = -1
            for row in cur:
                currentStatus = row['status']
            if sale['status'] == 0 and currentStatus != 0:
                warehouse = sale['warehouse']
                for product in bundle['products']:
                    cur.execute('select * from Warehouse_has_Product where Product_idProduct = "'
                    + product['Product_idProduct']+ '"' + ' AND Size_code = "' +
                    product['productSize'] + '"' + ' AND Warehouse_idWarehouse = '+ str(warehouse))

                    stock = 0
                    for row in cur:
                        stock = row['stock']

                    stock += product['quantity']
                    
                    query = 'UPDATE Warehouse_has_Product SET stock = ' + str(stock)
                    query += ' WHERE Warehouse_idWarehouse = '+ str(warehouse)
                    query += ' and Product_idProduct = "' + product['Product_idProduct']
                    query += '" and Size_code = "' + product['productSize'] + '"'
                    logger.info(query)
                    cur.execute(query)

            if 'promotion' in sale:
                promotion = sale['promotion']
                if promotion['status'] == 0:
                    query = 'update Promotions set status = '+str(promotion['status'])
                    query += ', count = ' + str(promotion['count'])
                    query += ' where code = "' + promotion['code'] + '"'
                else:
                    query = 'update Promotions set count = '+str(promotion['count'])
                    query += ' where code = "' + promotion['code'] + '"'
                cur.execute(query)
                
            query = 'update Sale set status = '+str(sale['status'])
            query += ' where idSale = ' + str(sale['idSale'])
            cur.execute(query)
            conn.commit()
            conn.close()
            return {
                'statusCode': 200,
                'headers': { 'Content-Type': 'application/json' },
                'body': event['body']
            }
