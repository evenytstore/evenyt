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
    This function obtains a Customer given an ID from the RDS instance or
    inserts a new customer.
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
        if event['httpMethod'] == 'GET':
            if event['pathParameters'] is not None:
                codePromotion = event['pathParameters']['code']
                cur.execute('select * from Promotions where code = "' + codePromotion + '"')
                promotion = None
                for row in cur:
                    promotion = row
                if promotion is None:
                    return {
                        'statusCode': 400,
                        'headers': { 'Content-Type': 'application/json' },
                        'body': 'Code '+codePromotion+' not found.'
                    }
                
                conn.close()
                return {
                    'statusCode': 200,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': json.dumps(promotion, cls=DateTimeEncoder, encoding='latin1')
                }
            else:
                conn.close()
                return {
                    'statusCode': 404,
                    'headers': { 'Content-Type': 'application/json' },
                    'body': 'not found.'
                }
