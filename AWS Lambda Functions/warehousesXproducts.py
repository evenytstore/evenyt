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
    This function obtains the brands from the RDS instance.
    """
    try:
        conn = pymysql.connect(rds_host, user=name,
                               passwd=password, db=db_name, connect_timeout=5)
    except Exception as e:
        sys.exit()

    product_list = []
    with conn.cursor(pymysql.cursors.DictCursor) as cur:
        cur.execute("select * from Product_has_Size limit 1000")
        
        for row in cur:
            product_list.append(row)

        for product in product_list:
            print(product)
            query = 'insert into Warehouse_has_Product (Warehouse_idWarehouse, Product_idProduct, '
            query += 'stock, Size_code) values(1, "' + product['Product_code'] + '", 0, "'
            query += product['Size_code'] + '")'
            cur.execute(query)

    conn.commit()
    conn.close()

lambda_handler(None, None)
