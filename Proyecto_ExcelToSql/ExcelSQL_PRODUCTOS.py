''' Sube y actualiza los productos a la base de datos SQL
'''

import xlrd #EXCEL
import pymysql #SQL
import boto3
from botocore.client import Config
import time
import getpass

def SubeImagen(path,name):
    ACCESS_KEY_ID = 'AKIAIRNMEOSSXOCFHKZA'
    ACCESS_SECRET_KEY = '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'
    BUCKET_NAME = 'evenytstore'
    FILE_NAME = name

    #data = open('.\Imagenes\\'+FILE_NAME, 'rb')
    data = open(path, 'rb')

    s3 = boto3.resource(
        's3',
        aws_access_key_id=ACCESS_KEY_ID,
        aws_secret_access_key=ACCESS_SECRET_KEY,
        config=Config(signature_version='s3v4')
    )

    s3.Bucket(BUCKET_NAME).put_object(Key=FILE_NAME, Body=data, ContentType='image/jpeg')  ##inserta la imagen
    s3.ObjectAcl(BUCKET_NAME, FILE_NAME).put(ACL='public-read')  # Lo hace público
''' Defino Variables:
'''
firstrow = int(2) #Declaro fila de inicio.
firstcolumn = int(1) #Declaro columna de inicio.

workbook = xlrd.open_workbook("SubirProductos.xlsx") #Lee libro
worksheet = workbook.sheet_by_name("PRODUCTOS") #Lee hoja
numRow = worksheet.nrows #numero de filas de la hoja
print(numRow)
#La celda A1 es cell (0,0)

#Variables constantes por el momento como Local (idWarehouse y sizecode)

ruta =".\IMAGES\\"

for i in range(firstrow, numRow):
    code = str(round(worksheet.cell(i, firstcolumn).value))


    #PARA LEER DATOS DE SQL

    conn1 = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin', passwd='LastOne1*', db='mainDataBase')
    cur1 = conn1.cursor()
    cur1.execute("SELECT code FROM mainDataBase.Product WHERE code='"+code+"';") #Busca si el codigo ya existe en la base de datos
    
    #si num_reg es cero se debe insertar un valor 
    if cur1.rowcount == 0: #Da cero cuando curl.execute no encontró un valor code dentro de la base.
        name = str(worksheet.cell(i, firstcolumn + 1).value)
        des = str(worksheet.cell(i, firstcolumn + 2).value)
        shortDescription = str(worksheet.cell(i, firstcolumn + 3).value)
        route_image = str(worksheet.cell(i, firstcolumn + 4).value)
        imgSrc = code + ".jpg"
        DateLastUpdate = str(worksheet.cell(i, firstcolumn + 5).value)
        Category_code = str(
            round(worksheet.cell(i, firstcolumn + 7).value))  # debe existir en la base de datos de Category
        SubCategory_code = str(
            round(worksheet.cell(i, firstcolumn + 9).value))  # debe existir en la base de datos de SubCategory
        Brand_code = str(round(worksheet.cell(i, firstcolumn + 11).value))  # debe existir en la base de datos de Brand
        BrandForm_code = str(
            round(worksheet.cell(i, firstcolumn + 12).value))  # debe existir en la base de datos de BrandForm
        size_code = str(round(worksheet.cell(i, firstcolumn + 14).value))
        Precio = str(worksheet.cell(i, firstcolumn + 15).value)
        # idWarehouse = str(round(worksheet.cell(i, firstcolumn + 16).value))
        idWarehouse = str(1)
        Sector = str(worksheet.cell(i, firstcolumn + 17).value)
        Stock = round(worksheet.cell(i, firstcolumn + 18).value)
        print("el precio es:" + Precio)
        cur3 = conn1.cursor()

        '''Falta añadir que comience con 1 y que sea automáico ser mayor al ultimo de la base de datos '''

        #Insertar en la clase de producto
        query3 = "INSERT INTO  mainDataBase.Product(code,name,description,shortDescription,imgSrc,DateLastUpdate,"
        query3 += "Category_code,SubCategory_code,Brand_code,BrandForm_code)"
        query3 += "VALUES ('"+code+"','"+name+"','"+des+"','"+shortDescription+"','"+imgSrc+"','"
        query3 += DateLastUpdate+"','"+Category_code+"','"+SubCategory_code+"','"+Brand_code+"','"+BrandForm_code+"');"
        cur3.execute(query3)
        conn1.commit()
        time.sleep(0.2)
        #Insertar en la clase de Warehouse*Product*Sector (idWarehouse,Productcode,sectorCode,stock,size)
        query4 = "INSERT INTO  mainDataBase.Warehouse_has_Product_has_Sector VALUES("+idWarehouse+",'"+code+"','"+Sector+"',"+str(Stock)+",'"+size_code+"');"
        cur3.execute(query4)
        conn1.commit()
        #Insertar en la clase de Warehouse*Product (idWarehouse,ProductidProduct,stock,size)
        query5 = "INSERT INTO  mainDataBase.Warehouse_has_Product VALUES("+idWarehouse+",'"+code+"',"+str(Stock)+",'"+size_code+"');"
        cur3.execute(query5)
        conn1.commit()
        #Insertar precio
        query6 = "insert into mainDataBase.Product_has_Size VALUES('"+code+"','"+size_code+"',"+Precio+");"
        cur3.execute(query6)
        conn1.commit() #ejecuta un insert o un update, para select no es necesario
        print("se añadio")
        cur3.close()
        #Sube imagen:
        path = ruta+route_image+".jpg"
        SubeImagen(path, imgSrc)

    #si no se debe actualizar (EL PRODUCTO YA EXISTE)   
    else:
        size_code = str(round(worksheet.cell(i, firstcolumn + 14).value))
        Precio = str(worksheet.cell(i, firstcolumn + 15).value)
        # idWarehouse = str(round(worksheet.cell(i, firstcolumn + 16).value))
        idWarehouse = str(1)
        Sector = str(worksheet.cell(i, firstcolumn + 17).value)
        Stock = round(worksheet.cell(i, firstcolumn + 18).value)
        print("el precio es:" + Precio)
        cur2 = conn1.cursor()
        cur3 = conn1.cursor()
        ''' ACTUALIZAR DATOS DEL PRODUCTO
        print ("se comienza a actualizar")
        query2 = "UPDATE mainDataBase.Product SET code='"+code+"', name='"+name+"',description='"+description
        query2 += "',shortDescription='"+shortDescription+"',imgSrc='"+imgSrc+"',DateLastUpdate='"+DateLastUpdate
        query2 += "',Category_code='"+Category_code+"',SubCategory_code='"+SubCategory_code+"',Brand_code='"+Brand_code
        query2 += "',BrandForm_code='"+BrandForm_code+"' WHERE code='"+code+"';"
        cur2.execute(query2)
        conn1.commit()
        '''
        #Leer el stock anterior de warehouse*Product
        
        #Si coincide con tamanho 
        cur3.execute(" SELECT stock FROM mainDataBase.Warehouse_has_Product where Warehouse_idWarehouse="+idWarehouse\
                     +" and Product_idProduct='"+code+"' and Size_code='"+size_code+"'; ")
        if cur3.rowcount != 0:
            stock_ant_tot = cur3.fetchall()[0][0]
            stock_actu_tot = str(stock_ant_tot + Stock)
            cur2.execute("UPDATE mainDataBase.Warehouse_has_Product SET stock="+stock_actu_tot\
                         +" WHERE Warehouse_idWarehouse="+idWarehouse+" and Product_idProduct='"+code\
                         +"' and Size_code='"+size_code+"';")
            conn1.commit()
            print("se actualizó stock TOTAL")
        else: # Se añade el precio al tamaño aumentado
            cur2.execute(
                "INSERT INTO mainDataBase.Product_has_Size VALUES(" + code + ",'" + size_code + "'," + Precio + ");")
            conn1.commit()
            query = "INSERT INTO  mainDataBase.Warehouse_has_Product VALUES(" + idWarehouse + ",'" + code + "'," + str(
                Stock) + ",'" + size_code + "');"
            cur3.execute(query)
            conn1.commit()

        #Si coincide con sector
        cur3.execute(" SELECT stock FROM mainDataBase.Warehouse_has_Product_has_Sector where Warehouse_idWarehouse=" \
                     +idWarehouse+" and Product_code='"+code+"' and Size_code='"+size_code+"' and Sector_code='"+Sector+"'; ")
        if cur3.rowcount != 0:
            stock_ant_sector = cur3.fetchall()[0][0]
            stock_actu_sector = str(stock_ant_sector + Stock)
            cur2.execute("UPDATE mainDataBase.Warehouse_has_Product_has_Sector SET stock="+stock_actu_sector+ \
                         " WHERE Warehouse_idWarehouse="+idWarehouse+" and Product_code='"+code+"' and Size_code='" \
                         +size_code+"' and Sector_code='"+Sector+"'; ")
            conn1.commit()
            print("se actualizo stock SECTOR")
        else:
            cur2.execute("INSERT INTO  mainDataBase.Warehouse_has_Product_has_Sector VALUES("+ \
                         idWarehouse+",'"+code+"','"+Sector+"',"+str(Stock)+",'"+size_code+"');")
            conn1.commit()
            print("se añadio stock a NUEVO SECTOR")
        '''
        if Precio != "":
            cam_precio = input('¿Desea cambiar el precio? (Y/N) \n ')

            if cam_precio == 'Y'or cam_precio == 'y':
                print("Esta por cambiar el precio de un producto")
                contra = input("Indica tu contraseña: \n")
                if contra == "pichon456":
                    cur2.execute("update mainDataBase.Product_has_Size set price="+Precio+" where Product_code='"+code+\
                                 "'and Size_code='"+size_code+"';")
                    conn1.commit()
                    print("Se cambio precio con exito")
        '''
        
        print("se actualizo")
        cur2.close()
        cur3.close()
    cur1.close()
    conn1.close()
    
 


'''
#PARA INSERTAR ALGUN DATO EN LA BASE DE DATOS
conn=pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com',port=3306,user='admin',passwd='LastOne1*',db='mainDataBase')
cur3=conn.cursor()
cur3.execute("INSERT INTO  mainDataBase.Cost(idCost,cost,frequencyDays,lastPay,nextPay)VALUES ('"+idCost+"',"+cost+","+frecDay+",'"+lastPay+"','"+nextPay+"');")
conn3.commit()
cur3.close()
conn3.close()
'''
print("se termino con exito")



