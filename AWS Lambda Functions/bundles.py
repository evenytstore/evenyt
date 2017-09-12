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
            bundle = json.loads(event['body'])
            
            query = 'insert into Bundle (name, frequencyDays, description, '
            query += 'lastOrdered, nextDelivery, preferredHour, '
            query += 'Customer_idCustomer) values("'
            query += bundle['name']+'", '+str(bundle['frequencyDays'])
            lastOrdered = datetime.datetime.strptime(bundle['lastOrdered'], "%d/%m/%Y")
            nextDelivery = datetime.datetime.strptime(bundle['nextDelivery'], "%d/%m/%Y")
            preferredHour = datetime.datetime.strptime(bundle['preferredHour'], "%d/%m/%Y")
            query += ', "'+bundle['description']+'", "'+lastOrdered.strftime('%Y-%m-%d')
            query += '", "'+nextDelivery.strftime('%Y-%m-%d')
            query += '", "'+preferredHour.strftime('%Y-%m-%d')
            query += '", '+str(bundle['Customer_idCustomer'])+')'
            cur.execute(query)
            idBundle = cur.lastrowid
            conn.commit()

            event['body']['idBundle'] = idBundle

            return {
                'statusCode': 200,
                'headers': { 'Content-Type': 'application/json' },
                'body': event['body']
            }
            
        else:
            if event['pathParameters'] is not None:
                if event['pathParameters']['idBundle'] is not None:
                    idBundle = event['pathParameters']['idBundle']
                    cur.execute('select * from Bundle where idBundle = ' + idBundle + '')
                    bundle = None
                    for row in cur:
                        bundle = row
                        
                    return {
                        'statusCode': 200,
                        'headers': { 'Content-Type': 'application/json' },
                        'body': json.dumps(bundle, cls=DateTimeEncoder, encoding='latin1')
                    }
                elif event['pathParameters']['idCustomer'] is not None:
                    idCustomer = event['pathParameters']['idCustomer']
                    idCustomer = idCustomer.replace("_",":")
                    cur.execute('select * from Bundle where Customer_idCustomer = "' + idCustomer + '"')
                    bundles = []
                    for row in cur:
                        bundles.append(row)
                        
                    return {
                        'statusCode': 200,
                        'headers': { 'Content-Type': 'application/json' },
                        'body': json.dumps(bundles, cls=DateTimeEncoder, encoding='latin1')
                    }
                else:
                    return {
                        'statusCode': 404,
                        'headers': { 'Content-Type': 'application/json' },
                        'body': 'not found.'
                    }
            else:
                return {
                    'statusCode': 404,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': 'not found.'
                }
