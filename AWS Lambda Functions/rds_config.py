db_username = "admin"
db_password = "LastOne1*"
db_name = "mainDataBase"
db_endpoint = "maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com"
db_port = 3306

from datetime import datetime
import json

class DateTimeEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, datetime):
            return o.isoformat()

        return json.JSONEncoder.default(self, o)
