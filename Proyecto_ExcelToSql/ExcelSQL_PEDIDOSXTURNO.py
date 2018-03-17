''' Obtengo los productos por turno:
'''

import xlrd #EXCEL
import pymysql #SQL

''' Defino Variables:
'''
turno = '2017-10-21 00:00:00'

#Función para crear matrices:
def creatematrix(n,m):
 a = [0]*m
 matriz = [a]*n
 return matriz
print('gg')

#PARA LEER DATOS DE SQL:

#Conecto con la base y la guardo en conn1:
conn1 = pymysql.connect(host = 'maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port = 3306, user = 'admin', passwd = 'LastOne1*', db = 'mainDataBase')
#Asigno la base leída a la variable curl:
cur1 = conn1.cursor()
#Busco dentro de esa base la información seleccionada de las filas que coinciden con el turno elegido:
cur1.execute("SELECT Bundle_idBundle, Product_idProduct, quantity, Address_idAddress, Subtotal FROM mainDataBase.Product_has_Bundle WHERE dateOrder='"+turno+"';") #Busca que bundle corresponden al turno deseado.

matriz = cur1.fetchall() #Guardo la matriz obtenida.
idBundle = creatematrix(len(matriz),1)

i = 0;
for m in matriz:
    idBundle[i] = m[0]
    i = i + 1
    print(m[0])
i = 0

#Evalúo idBundles repetidos y los reemplazo por una variable.
reemplazo = -1;
for j in range(0, len(idBundle)):
    if idBundle[j] != reemplazo: #No realizar lectura si el dato ya fue reemplazado
        for k in range(0, len(idBundle)):
            if j != k: #Para no repetir lectura:
                if idBundle[j] == idBundle[k]:
                    if k + 1 < len(idBundle):
                        idBundle[k] = idBundle[k+1]
                        idBundle[k+1] = reemplazo
                    else:
                        idBundle[k] = reemplazo

#Obengo idProduct por idBundle:
for m in matriz:
    if m[0] == idBundle[0]:
        print(m[1])

print(idBundle)


#curl.rowcount indica el número de comparaciones exitosas, si hay más de una se obtiene el número de bundle:
if cur1.rowcount != 0:
    gg = cur1.rowcount
    print(str(gg))
    

#Cierro conexión:
cur1.close()
conn1.close()

'''
workbook = xlrd.open_workbook("SubirProductos.xlsx") #Lee libro
worksheet = workbook.sheet_by_name("PRODUCTOS") #Lee hoja
numRow = worksheet.nrows #numero de filas de la hoja
print(numRow)
#La celda A1 es cell (0,0)

for i in range(firstrow,numRow):
    code = str(round(worksheet.cell(i,firstcolumn).value))
    name = str((worksheet.cell(i,firstcolumn + 1).value))
    description = str((worksheet.cell(i,firstcolumn + 2).value))
    shortDescription = str((worksheet.cell(i,firstcolumn + 3).value))
    imgSrc = str((worksheet.cell(i,firstcolumn + 4).value))
    DateLastUpdate = str((worksheet.cell(i,firstcolumn + 5).value))
    Category_code = str(round(worksheet.cell(i,firstcolumn + 6).value)) #debe existir en la base de datos de Category
    SubCategory_code = str(round(worksheet.cell(i,firstcolumn + 7).value)) #debe existir en la base de datos de SubCategory
    Brand_code = str(round(worksheet.cell(i,firstcolumn + 8).value)) #debe existir en la base de datos de Brand
    BrandForm_code = str(round(worksheet.cell(i,firstcolumn + 9).value)) #debe existir en la base de datos de BrandForm
    

    #PARA LEER DATOS DE SQL

    conn1 = pymysql.connect(host = 'maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port = 3306, user = 'admin', passwd = 'LastOne1*', db = 'mainDataBase')
    cur1 = conn1.cursor()
    cur1.execute("SELECT code FROM mainDataBase.Product WHERE code='"+code+"';") #Busca si el codigo ya existe en la base de datos
    
    #si num_reg es cero se debe insertar un valor 
    if cur1.rowcount == 0: #Da cero cuando curl.execute no encontró un valor code dentro de la base.
        cur3 = conn1.cursor()
        query3 = "INSERT INTO  mainDataBase.Product(code,name,description,shortDescription,imgSrc,DateLastUpdate,"
        query3 += "Category_code,SubCategory_code,Brand_code,BrandForm_code)"
        query3 += "VALUES ('"+code+"','"+name+"','"+description+"','"+shortDescription+"','"+imgSrc+"','"
        query3 += DateLastUpdate+"','"+Category_code+"','"+SubCategory_code+"','"+Brand_code+"','"+BrandForm_code+"');"
        cur3.execute(query3) 
        conn1.commit()#ejecuta un insert o un update, para select no es necesario
        print("se añadio")
        cur3.close()
    #si no se debe actualizar    
    else:
        cur2=conn1.cursor()
        print ("se comienza a actualizar")
        query2 = "UPDATE mainDataBase.Product SET code='"+code+"', name='"+name+"',description='"+description
        query2 += "',shortDescription='"+shortDescription+"',imgSrc='"+imgSrc+"',DateLastUpdate='"+DateLastUpdate
        query2 += "',Category_code='"+Category_code+"',SubCategory_code='"+SubCategory_code+"',Brand_code='"+Brand_code
        query2 += "',BrandForm_code='"+BrandForm_code+"' WHERE code='"+code+"';"
        cur2.execute(query2)
        conn1.commit()
        print ("se actualizo")
        cur2.close()
    

    cur1.close()
    conn1.close()
    
 


'''
'''
#PARA INSERTAR ALGUN DATO EN LA BASE DE DATOS
conn=pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com',port=3306,user='admin',passwd='LastOne1*',db='mainDataBase')
cur3=conn.cursor()
cur3.execute("INSERT INTO  mainDataBase.Cost(idCost,cost,frequencyDays,lastPay,nextPay)VALUES ('"+idCost+"',"+cost+","+frecDay+",'"+lastPay+"','"+nextPay+"');")
conn3.commit()
cur3.close()
conn3.close()
'''
print(turno)



