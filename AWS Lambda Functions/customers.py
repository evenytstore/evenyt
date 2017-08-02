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

try:
    conn = pymysql.connect(rds_host, user=name,
                           passwd=password, db=db_name, connect_timeout=5)
except Exception as e:
    logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
    logger.error(e)
    sys.exit()

logger.info("SUCCESS: Connection to RDS mysql instance succeeded")
def lambda_handler(event, context):
    """
    This function obtains a Customer given an ID from the RDS instance or
    inserts a new customer.
    """

    with conn.cursor(pymysql.cursors.DictCursor) as cur:
        if event['httpMethod'] == 'POST':
            customer = json.loads(event['body'])
            address = customer['address']
            
            query = 'insert into Address (addressName, addressNumber, '
            query += 'latitude, longitude, district, city) values("'
            query += address['addressName']
            query += '", "'+address['addressNumber']+'", '+str(address['latitude'])
            query += ', '+str(address['longitude'])+', "'+address['district']
            query += '", "'+address['city']+'")'
            cur.execute(query)

            idAddress = cur.lastrowid
            
            query = 'insert into Customer (idCustomer, name, lastName, email, '
            query += 'phoneNumber, DNI, RUC, birthday, Address_idAddress) values("'
            query += customer['idCustomer']+'", "'+customer['name']
            query += '", "'+customer['lastName']+'", "'+customer['email']
            query += '", "'+customer['phoneNumber']+'", "'+customer['DNI']
            birthday = datetime.datetime.strptime(customer['birthday'], "%d/%m/%Y")
            query += '", "'+customer['RUC']+'", "'+birthday.strftime('%Y-%m-%d')
            query += '", '+str(idAddress)+')'
            cur.execute(query)
            conn.commit()

            return {
                'statusCode': 200,
                'headers': { 'Content-Type': 'application/json' },
                'body': event['body']
            }
            
        elif event['httpMethod'] == 'GET':
            if event['pathParameters'] is not None:
                idCustomer = event['pathParameters']['idCustomer']
                cur.execute('select * from Customer where idCustomer = "' + idCustomer + '"')
                customer = None
                for row in cur:
                    customer = row
                if customer is None:
                    return {
                        'statusCode': 404,
                        'headers': { 'Content-Type': 'application/json' },
                        'body': 'Not found.'
                    }
                return {
                    'statusCode': 200,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': json.dumps(customer, cls=DateTimeEncoder, encoding='latin1')
                }
            else:
                return {
                    'statusCode': 404,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': 'Not found.'
                }
