#Librerias a usar:
import pymysql #Libreria SQL

#Enuncio variables:
turno = "2017-10-21 00:00:00"
salida = 0 #Variable que usaré para "salir" de un loop
reemplazo = -1 #Variable que reemplaza valores repetidos

#Establezco conexión:
conn=pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com',port=3306,user='admin',passwd='LastOne1*',db='mainDataBase')
#Cargo base de datos en un cursor:
cur=conn.cursor()
#Obtengo lista con turno deseado:
cur.execute("SELECT Bundle_idBundle FROM mainDataBase.Product_has_Bundle WHERE dateOrder = '" + turno + "'")

#Copio matriz obtenida:
mat = []
for m in cur.fetchall():
    mat.append(m[0])
#Defino la matriz donde guardaré los idBundle obtenidos:
idBundle = []


#Reemplazo valores de matriz mat repetidos:
for i in range(0, len(mat)):
    if mat[i] != reemplazo:
        for j in range(0, len(mat)):
            if i != j:
               if mat[i] == mat[j]:
                   mat[j] = reemplazo

#Obtengo matriz con idBundle sin repetir
for i in range(0,len(mat)):
    if mat[i] != reemplazo:
        idBundle.append(str(mat[i]))
#Ordeno los idBundle consecutivamente (burbuja):
for i in range(0,len(idBundle)):
        for j in range(j, len(idBundle)):
            if idBundle[i] > idBundle[j]:
                bandera = idBundle[i]
                idBundle[i] = idBundle[j]
                idBundle[j] = bandera

#Obtengo lista de productos por bundle:
for i in range(0, len(idBundle)):
    cur.execute("SELECT Product_idProduct,Quantity FROM mainDataBase.Product_has_Bundle WHERE Bundle_idBundle = '" + idBundle[i] + "'")
    #Copio matriz obtenida:
    matr = []
    quantity = []

    for m in cur.fetchall():
        matr.append(m[0])
        quantity.append(m[1])
        

    print('PEDIDO ' + str(i+1) + ':')
    print();

    #Consulto sectores:
    Sector = []
    CodProdSector = []
    for j in range(0, len(matr)):
        #Consulto sector usando código de producto:
        cur.execute("SELECT Sector_code FROM mainDataBase.Warehouse_has_Product_has_Sector WHERE Product_code = '" + matr[j] + "'")
        Sector.append(cur.fetchall()[0][0])
        cur.execute("SELECT Product_code FROM mainDataBase.Warehouse_has_Product_has_Sector WHERE Product_code = '" + matr[j] + "'")
        CodProdSector.append(cur.fetchall()[0][0])
    #Ordeno sectores consecutivamente junto con sus códigos:
    for j in range(0,len(Sector)):
        for k in range(j, len(Sector)):
            if Sector[j] > Sector[k]:
                bandera = Sector[j]
                bandera2 = CodProdSector[j]
                bandera3 = quantity[j]
                Sector[j] = Sector[k]
                CodProdSector[j] = CodProdSector[k]
                quantity[j] = quantity[k]
                Sector[k] = bandera
                CodProdSector[k] = bandera2
                quantity[k] = bandera3
    print('SECTOR ' + str(Sector[0]) + ':')
    #Busco pedidos por sectores:
    for j in range(0, len(Sector)):
        Product = []
        if Sector[j] == Sector[0]:
            #Consulto nombre y descripción de producto usando código de producto:
            cur.execute("SELECT name, description FROM mainDataBase.Product WHERE code = '" + CodProdSector[j] + "'")
            Product.append(cur.fetchall())
            #Imprimo productos con su descripción:
            for m in Product:
                print('*CANTIDAD '+str(quantity[j]))
                for sumador in range(0, quantity[j]):
                    print('- ' + m[0][0] + ', ' + m[0][1])
    for j in range(0, len(Sector)):
        for k in range(0, len(Sector)):
            if j != k:
                if Sector[j] != Sector[k]:
                    if j > 0:
                        print('SECTOR ' + str(Sector[j]) + ':')
                        for z in range(0, len(Sector)):
                            Product = []
                            if Sector[z] == Sector[j]:
                                #Consulto nombre y descripción de producto usando código de producto:
                                cur.execute("SELECT name, description FROM mainDataBase.Product WHERE code = '" + CodProdSector[j] + "'")
                                Product.append(cur.fetchall())
                                #Imprimo productos con su descripción:
                                for m in Product:
                                    print('*CANTIDAD '+str(quantity[j]))
                                    for sumador in range(0, quantity[j]):
                                        print('- ' + m[0][0] + ', ' + m[0][1])
                                                               
        
    #Consulto dirección del pedido:
    cur.execute("SELECT Address_idAddress FROM mainDataBase.Product_has_Bundle WHERE Bundle_idBundle = '" + idBundle[i] + "'")
    Direccion = str(cur.fetchall()[0][0])
    cur.execute("SELECT addressName, addressNumber, district FROM mainDataBase.Address WHERE idAddress = '" + Direccion + "'")
    Address = cur.fetchall()
    print();
    print('DIRECCIÓN:')
    print('- ' + Address[0][0] + ' ' + Address[0][1] + ' - ' + Address[0][2])
    print();
    print('ID BUNDLE: ' + idBundle[i])
    print();
    
    print(); 

#Cierro conexiones:
cur.close()
conn.close()

    
