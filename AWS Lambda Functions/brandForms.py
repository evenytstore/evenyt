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

def lambda_handler(event, context):
    """
    This function obtains the brand forms from the RDS instance.
    """

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

    brand_forms_list = []
    with conn.cursor(pymysql.cursors.DictCursor) as cur:
        '''cur.execute("create table Employee3 (EmpID  int NOT NULL, Name varchar(255) NOT NULL, PRIMARY KEY (EmpID))")
        cur.execute('insert into Employee3 (EmpID, Name) values(1, "Joe")')
        cur.execute('insert into Employee3 (EmpID, Name) values(2, "Bob")')
        cur.execute('insert into Employee3 (EmpID, Name) values(3, "Mary")')
        conn.commit()'''
        cur.execute("select * from BrandForm")
        for row in cur:
            brand_forms_list.append(row)

    conn.close()
    return {
        'statusCode': 200,
        'headers': { 'Content-Type': 'application/json' },
        'body': json.dumps(brand_forms_list, cls=DateTimeEncoder, encoding='latin1')
    }
