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

    with conn.cursor(pymysql.cursors.DictCursor) as cur:
        if event['httpMethod'] == 'POST':
            sale = json.loads(event['body'])
            bundle = sale['bundle']

            query = 'insert into Bundle (name, frequencyDays, description, '
            query += 'lastOrdered, nextDelivery, preferredHour, '
            query += 'Customer_idCustomer) values("'
            query += bundle['name']+'", '+str(bundle['frequencyDays'])
            lastOrdered = datetime.datetime.strptime(bundle['lastOrdered'], "%d/%m/%Y")
            nextDelivery = datetime.datetime.strptime(bundle['nextDelivery'], "%d/%m/%Y")
            preferredHour = datetime.datetime.strptime(bundle['preferredHour'], "%d/%m/%Y %H")
            query += ', "'+bundle['description']+'", "'+lastOrdered.strftime('%Y-%m-%d')
            query += '", "'+nextDelivery.strftime('%Y-%m-%d')
            query += '", "'+preferredHour.strftime('%Y-%m-%d %H')
            query += '", "'+str(bundle['Customer_idCustomer'])+'")'

            cur.execute(query)
            idBundle = cur.lastrowid

            for product in bundle['products']:
                address = product['address']
            
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
                cur.execute(query)
            
            query = 'insert into Sale (total, rating, status, '
            query += 'Bundle_idBundle, Bundle_Customer_idCustomer, '
            query += 'typeSale_idtypeSale, Evener_idEvener) values('
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
            query += ', '+str(sale['Evener_idEvener'])+')'
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
