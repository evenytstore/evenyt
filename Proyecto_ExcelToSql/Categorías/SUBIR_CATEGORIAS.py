''' Sube y actualiza los productos a la base de datos SQL
'''

import xlrd #EXCEL
import pymysql #SQL


''' Defino Variables:
'''
firstrow = int(2) #Declaro fila de inicio.
firstcolumn = int(1) #Declaro columna de inicio.

workbook = xlrd.open_workbook("CATEGORIAS.xlsx") #Lee libro
worksheet = workbook.sheet_by_name("CATEGORIAS") #Lee hoja
numRow = worksheet.nrows #numero de filas de la hoja
print(numRow)
#La celda A1 es cell (0,0)

conn1 = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin', passwd='LastOne1*', db='mainDataBase')
cur1 = conn1.cursor()

for i in range(firstrow, numRow):
    code = str(round(worksheet.cell(i, firstcolumn).value))
    name = str(worksheet.cell(i, firstcolumn + 1).value)
    des = str(worksheet.cell(i, firstcolumn + 2).value)
    query = "insert into mainDataBase.Category values ('"+code+"','"+name+"','"+des+"');"
    cur1.execute(query)
    conn1.commit()

cur1.close()
conn1.close()



