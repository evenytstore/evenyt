import sys
import logging
import rds_config
import pymysql
import json
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
    This function obtains the productsXSizes from the RDS instance.
    """

    try:
        conn = pymysql.connect(rds_host, user=name,
                               passwd=password, db=db_name, connect_timeout=5)
    except Exception as e:
        logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
        logger.error(e)
        sys.exit()

    logger.info("SUCCESS: Connection to RDS mysql instance succeeded")

    products_list = []
    with conn.cursor(pymysql.cursors.DictCursor) as cur:
        cur.execute("select * from Product_has_Size limit 1000")
        for row in cur:
            products_list.append(row)

    conn.close()
    return {
        'statusCode': 200,
        'headers': { 'Content-Type': 'application/json' },
        'body': json.dumps(products_list, cls=DateTimeEncoder, encoding='latin1')
    }
