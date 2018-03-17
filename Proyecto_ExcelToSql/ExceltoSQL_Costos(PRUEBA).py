import xlrd #EXCEL
import pymysql #SQL


#workbook=xlrd.open_workbook("Prueba.xlsm") #con macros
workbook=xlrd.open_workbook("PruebaSinMacro.xlsx") #sin macros
#print ("cargo libro")
worksheet=workbook.sheet_by_name("COSTOS")
print("cargo hoja")
numRow=worksheet.nrows
print(numRow)
# worksheet=workbook.sheet_by_index(0)
#La celda A1 es cell (0,0)
#print("the value at row 1 and column 1 is:{0}".format(worksheet.cell(1,1).value))
for i in range(2,numRow):
    idCost=(worksheet.cell(i,1).value)
    cost=(worksheet.cell(i,2).value)
    frecDay=(worksheet.cell(i,3).value)
    lastPay=(worksheet.cell(i,4).value)
    nextPay=(worksheet.cell(i,5).value)

 #se cambia a string el valor leido
    idCost=str(idCost)
    cost=str(cost)
    frecDay=str(frecDay)
    lastPay=str(lastPay)
    nextPay=str(nextPay)

    #PARA LEER DATOS DE SQL

    conn1=pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com',port=3306,user='admin',passwd='LastOne1*',db='mainDataBase')
    cur1=conn1.cursor()
    #print(cur1.description) #PARA VER DETALLES DE LA CONEXION
    cur1.execute("SELECT idCost FROM mainDataBase.Cost WHERE idCost="+idCost)
    num_reg= cur1.rowcount
    #print(num_reg)
    #si num_reg es cero se debe insertar un valor si no se debe actualizar
    if num_reg==0:
        cur3=conn1.cursor()
        print("se añadio")
        cur3.execute("INSERT INTO  mainDataBase.Cost(idCost,cost,frequencyDays,lastPay,nextPay)VALUES ('"+idCost+"',"+cost+","+frecDay+",'"+lastPay+"','"+nextPay+"');") 
        conn1.commit()#ejecuta un insert o un update, para select no es necesario 
        cur3.close()
    else:
        cur2=conn1.cursor()
        print ("se actualizo")
        cur2.execute("UPDATE mainDataBase.Cost SET cost="+cost+", frequencyDays="+frecDay+",lastPay='"+lastPay+"',nextPay='"+nextPay+"'WHERE idCost="+idCost+";")
        conn1.commit()
        cur2.close()
    

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



