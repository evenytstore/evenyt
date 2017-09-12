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
            sale = json.loads(event['body'])
            
            query = 'insert into Sale (total, rating, status, '
            query += 'Bundle_idBundle, Bundle_Customer_idCustomer, '
            query += 'typeSale_idTypeSale, Evener_idEvener) values('
            query += str(sale['total'])+', '+str(sale['rating'])
            query += ', '+str(sale['status'])+', '+str(sale['Bundle_idBundle'])
            query += ', '+str(sale['Bundle_Customer_idCustomer'])
            query += ', '+str(sale['typeSale_idTypeSale'])
            query += ', '+str(sale['Evener_idEvener'])+')'
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
                idCustomer = idCustomer.replace("_",":")
                cur.execute('select * from Sale where Bundle_Customer_idCustomer = "' + idCustomer + '"')
                sales = []
                for row in cur:
                    sales.append(row)
                    
                return {
                    'statusCode': 200,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': json.dumps(sales, cls=DateTimeEncoder, encoding='latin1')
                }
            else:
                return {
                    'statusCode': 404,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': 'not found.'
                }
