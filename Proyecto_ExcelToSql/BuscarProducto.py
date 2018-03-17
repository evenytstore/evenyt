import pymysql #SQL


conn = pymysql.connect(host = 'maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port = 3306, user = 'admin', passwd = 'LastOne1*', db = 'mainDataBase')
cur = conn.cursor()
bus = input("Escriba lo que desea buscar: \n")
buscan = bus.split()
query=""
n = len(buscan)
print(n)
cont=1
for m in buscan:
    if cont == len(buscan):
        m = "'%" + m + "%'"
    else:
        m = "'%" + m + "%' and description like"
    query+=m
    cont += 1
print(query)
if len(buscan)> 1:
    cur.execute("select * from mainDataBase.Product where description LIKE" + query) #Busca si el codigo ya existe en la base de datos
    mat=cur.fetchall()
    for a in mat:
        print(a[2])