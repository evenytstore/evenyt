#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pymysql #SQL
from tkinter import *
from tkinter import ttk
from tkinter import messagebox



    #root.iconify()

def Aviso1():
    messagebox.showwarning("Aviso","Seleccione Producto o sector o stock mayor a 0")
def Aviso2():
    messagebox.showwarning("Aviso","Escriba el producto a buscar")
def Aviso3(texto):
    messagebox.showwarning("Información",texto)

########## Evento cuando se hace clic en buscar
def Buscar(event):
    tree.delete(*tree.get_children())# limpia la grilla


    #bus = input("Escriba lo que desea buscar: \n")
    bus = producto.get()  # obtiene lo que esta escrito en el box
    print(bus)
    producto.set("")
    buscan = bus.split()
    if len(bus) == 0: #Si el combo el box esta vacio
        #print("Escriba producto a buscar")  # en ventana fata hacer
        Aviso2()
    else:
        conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                               passwd='LastOne1*', db='mainDataBase')
        cur = conn.cursor()
        query = ""
        n = len(buscan)
        # print(n)
        cont = 1
        for m in buscan:
            if cont == len(buscan):
                m = "'%" + m + "%'"
            else:
                m = "'%" + m + "%' and description like"
            query += m
            cont += 1
        # print(query)
        if len(buscan) > 1:
            cur.execute(
                "select * from mainDataBase.Product where description LIKE" + query)  # Busca si el codigo ya existe en la base de datos
            mat = cur.fetchall()
            i = cur.rowcount
            for a in mat:
                tree.insert('', 0, text=i, values=(a[0], a[2]))
                i -= 1
                # print(a[0])
        cur.close()
        conn.close()

#
################ Evento cuando se seleciona un elemento de la grilla
def Seleccion (event):
    global size_name_price
    global size_nameADD
    global size_name_priceADD
    size_name_priceADD = []
    i = tree.selection()[0]
    combo.set("Elija sector")
    comboTam.set("")
    stock.set("0")
    valuesCombo=["1A","1B","1C","1D","2A","2B","2C","2D","3A","3B","3C","3D"
                 "4A","4B","4C","4D","5A","5B","5C","5D","6A", "6B","7C","7D",
                 "8A","8B","8C","8D","9A","9B","9C","9D","10R","Centro"]
    combo["values"]=valuesCombo
    #print("hizo click")
    #print(i)
    #for child in tree.get_children(i):
    #print(tree.item(i)["values"][0])
    codigoProdu=tree.item(i)["values"][0]
    codPro.set(codigoProdu)
    Descri.set(tree.item(i)["values"][1])
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    query = "select A.Size_code, B.name, A.price "
    query += "from mainDataBase.Product_has_Size A, mainDataBase.Size B "
    query += "where Product_code = '"+str(codigoProdu)+"' and A.Size_code = B.code;"
    cur.execute(query)
    size_name_price = cur.fetchall()
    val_combo2 = []
    val_comboTam = []
    query1 = "SELECT code,name FROM mainDataBase.Size"
    cur.execute(query1)
    size_nameADD = cur.fetchall()## debe ser solo los que no se repiten
    for m in size_name_price:
        val_combo2.append(m[1])

    ###para eliminar los igulaes
    size_nameADD = list(size_nameADD)
    for a in size_nameADD[:]:
        for b in size_name_price:
            if a[0]==b[0]:
                size_nameADD.remove(a)

    size_nameADD = tuple(size_nameADD)
    for a in size_nameADD:
        val_comboTam.append(a[1])

    combo2["values"] = val_combo2



    comboTam["values"] = val_comboTam


def MostrarPrecio(event):
    tama = combo2.get()
    for m in size_name_price:
        if tama == m[1]:
            price.set(m[2])

def Agregar (event):
    code = codPro.get()
    Sector = combo.get()
    #print(Sector)
    combo.set("Elija sector")
    Stock = int(stock.get())

    if code != "" and Sector != "" and combo2.get() != "" and Sector != "Elija sector" and Stock > 0: ##Si esiste algun codigo de producto
        # Si coincide con tamanho
        conn1 = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                                passwd='LastOne1*', db='mainDataBase')
        cur2 = conn1.cursor()
        cur3 = conn1.cursor()
        # default por el momento
        idWarehouse = str(1)
        tama = combo2.get()
        for m in size_name_price:
            if tama == m[1]:
                size_code = m[0]

        ##se obtienen de los box
        Descri.set("")
        stock.set("0")
        codPro.set("")
        combo["values"] = []
        combo2["values"] = []
        combo2.set("")
        aviso = ""
        #Sector = sector.get()
        #sector.set("")

        cur3.execute(
            " SELECT stock FROM mainDataBase.Warehouse_has_Product where Warehouse_idWarehouse=" + idWarehouse\
            + " and Product_idProduct='" + code + "' and Size_code='" + size_code + "'; ")
        if cur3.rowcount != 0:
            stock_ant_tot = cur3.fetchall()[0][0]
            stock_actu_tot = str(stock_ant_tot + Stock)
            cur2.execute(
                "UPDATE mainDataBase.Warehouse_has_Product SET stock=" + stock_actu_tot \
                + " WHERE Warehouse_idWarehouse=" + idWarehouse + " and Product_idProduct='" \
                + code + "' and Size_code='" + size_code + "';")
            conn1.commit()
            aviso += "Se actualizó stock TOTAL de producto"+'\n'+ "tamaño "+tama+"había "+str(stock_ant_tot)\
                     +" ahora hay "+stock_actu_tot+'\n'
            #print("se actualizó stock TOTAL: HABIA "+str(stock_ant_tot)+" ahora "+stock_actu_tot)
            #Aviso3(aviso)
        # Si coincide con sector
        cur3.execute(
            " SELECT stock FROM mainDataBase.Warehouse_has_Product_has_Sector where Warehouse_idWarehouse=" \
            + idWarehouse + " and Product_code='" + code + "' and Size_code='" + size_code + "' and Sector_code='" + Sector + "'; ")
        if cur3.rowcount != 0:
            stock_ant_sector = cur3.fetchall()[0][0]
            stock_actu_sector = str(stock_ant_sector + Stock)
            cur2.execute(
                "UPDATE mainDataBase.Warehouse_has_Product_has_Sector SET stock=" + stock_actu_sector \
                + " WHERE Warehouse_idWarehouse=" + idWarehouse + " and Product_code='" + code + "' and Size_code='" \
                + size_code + "' and Sector_code='" + Sector + "'; ")
            conn1.commit()
            aviso += "Se actualizó stock en el  SECTOR "+ Sector+'\n'+" había "+str(stock_ant_sector)+", ahora hay "+stock_actu_sector
            #print("se actualizo stock SECTOR "+ Sector+" HABIA "+str(stock_ant_sector)+" ahora hay "+stock_actu_sector)
            Aviso3(aviso)
        else:
            cur2.execute(
                "INSERT INTO  mainDataBase.Warehouse_has_Product_has_Sector VALUES(" + idWarehouse + ",'" + code + "','" + Sector + "'," + str(
                    Stock) + ",'" + size_code + "');")
            conn1.commit()
            #print("se añadio stock a NUEVO SECTOR "+Sector+" y hay: "+str(Stock))
            aviso += "Se añadió stock a NUEVO SECTOR: "+Sector+'\n'+" y hay: "+str(Stock)
            Aviso3(aviso)
        cur2.close()
        cur3.close()
        conn1.close()

    else:
        print("Seleccione Producto o sector o stock mayor a 0")#en ventana fata hacer
        Aviso1()
def AgrTamaño(event):
    precio = priceAdd.get()
    precio = float(precio)
    if (isinstance(precio, (float))) and len(comboTam.get()) > 0:
        listbox.delete(0, END)
        for a in size_nameADD:
            if comboTam.get() == a[1]:
                codSize = a[0]
        listaPre = (codSize, comboTam.get(), precio)
        size_name_priceADD.append(listaPre)
        # print(size_name_priceADD)
        for i in size_name_priceADD:
            a = str(i[1]) + "    -->   " + str(i[2])
            listbox.insert(0, a)

        priceAdd.set("")
        price.set("")
        comboTam.set("")
    else:
        text = "debe ingresar un valor numerico e ingresar el tamaño"
        Aviso3(text)
def ActuTamaños(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    if len(size_name_priceADD) > 0:
        cod = str(codPro.get())
        text = "Se agregaron los siguientes tamaños al producto:\n"
        for a in size_name_priceADD:
            query = " INSERT INTO mainDataBase.Product_has_Size VALUES ('" + cod + "','" + a[0] + "'," + str(a[2]) + ");"
            cur.execute(query)
            conn.commit()
            text += a[1]+" al precio de:" + str(a[2]) + " soles"
        Descri.set("")
        stock.set("0")
        codPro.set("")
        combo["values"] = []
        combo2["values"] = []
        comboTam["values"] = []
        price.set("")
        comboTam.set("")
        combo2.set("")
        listbox.delete(0, END)
        cur.close()
        conn.close()
        Aviso3(text)
        #print("Done")
    else:
        Aviso3("DEBE AGREGAR AL MENOS TAMAÑO ")


    #
#########################VARIABLES GENERALES

######### Ventana Modificar Stock
ventana = Tk()
ventana.geometry('800x600+100+50')
#ventana.resizable(0,0)
ventana.title('Modificar stock')

#####Menu
'''
barramenu = Menu(ventana)
mnuOption = Menu(barramenu)
mnuOption.add_command(label="Editar Stock", command = Aviso1)
mnuOption.add_command(label="Agregar Producto", command= Aviso2)
'''

###########Labels y box
label1 = Label(ventana,text="Producto: ")
label1.grid(row=1,column=1,sticky=E)
producto = StringVar()
caja = Entry(ventana,textvariable=producto)
caja.grid(row=1,column=2,sticky=W)

label2 = Label(ventana,text="Código Producto: ")
label2.grid(row=7,column=1)
codPro = StringVar()
caja2 = Entry(ventana,textvariable=codPro,state=DISABLED)
caja2.grid(row=7,column=2,sticky=W)

label3 = Label(ventana,text="Descripción: ")
label3.grid(row=8,column=1,sticky=E)#Sticky ,este, oeste, norte, sur
Descri = StringVar()
caja3 = Entry(ventana,textvariable=Descri,state=DISABLED,width=50)
caja3.grid(row=8,column=2,sticky=W)

label6 = Label(ventana,text="Tamaño: ")
label6.grid(row=9,column=1,sticky=E)
label7 = Label(ventana,text="Precio ")
label7.grid(row=10,column=1,sticky=E)
price = StringVar()
caja4 = Entry(ventana,textvariable=price,state=DISABLED)
caja4.grid(row=10,column=2,sticky=W)
combo2 = ttk.Combobox(state="readonly")#para que el usuario no ingrese sus valores
combo2.grid(row=9,column=2,sticky=W)
combo2.bind('<<ComboboxSelected>>',MostrarPrecio)

label4 = Label(ventana,text="Sector: ")
label4.grid(row=7,column=3)
combo = ttk.Combobox(state="readonly")#para que el usuario no ingrese sus valores
combo.grid(row=7,column=4)

label5 = Label(ventana,text="Stock ingresado: ")
label5.grid(row=8,column=3)
stock = StringVar()
caja5 = Entry(ventana,textvariable=stock)
caja5.grid(row=8,column=4)


##########Botones
boton1 = Button(ventana,text="Buscar")
boton1.grid(row=3,column=2,columnspan=3)
boton2 = Button(ventana,text="Seleccionar")
boton2.grid(row=6,column=2,columnspan=3)
boton3 = Button(ventana,text="Añadir")
boton3.grid(row=9,column=4)

###########Grilla, cuadro donde se muestran valores
tree = ttk.Treeview (height=10, columns=1)
tree["columns"] = ("code", "Description")
tree.column("code", width=100)
tree.column("Description", width=300)
#tree.heading('#0',text = 'Codigo')
tree.heading("code", text="code")
tree.heading("Description", text="Description")
tree.grid(row=4,column=2,columnspan=3)

##########List Box
t="Solo utilizar si se quiere agregar más tamaños al producto selecionado "
verde1="#8EECA3"
l7 = Label(ventana,text=t,bg="#22EE4E")#relief="flat", borderwidth=5,'orange'
l7.grid(row=11,column=1,columnspan=3,sticky=W)
a=12
l6 = Label(ventana,text="Tamaño a añadir: ")
l6.grid(row=a,column=1,sticky=E)
listbox = Listbox(ventana,width=40)
listbox.grid(row=a+1,column=2,sticky=E)
boton5 = Button(ventana,text="AgregaTamaño")
boton5.grid(row=a,column=4)
boton5.bind('<Button-1>',AgrTamaño)
comboTam = ttk.Combobox(ventana,state="readonly")#para que el usuario no ingrese sus valores
comboTam.grid(row=a,column=2,sticky=W)
l8 = Label(ventana,text="Precio relacionado: ")
l8.grid(row=a,column=2,sticky=E)
priceAdd = StringVar()
caja6 = Entry(ventana,textvariable=priceAdd)
caja6.grid(row=a,column=3,sticky=W)
boton6 = Button(ventana,text="Actualizar\nTamaños")
boton6.grid(row=a+1,column=3,sticky=W)
boton6.bind('<Button-1>',ActuTamaños)

#################### Agregar los menus
'''
barramenu.add_cascade(label="Options",menu=mnuOption)
ventana.config(menu=barramenu)
'''

##############Eventos de los botores
boton1.bind('<Button-1>',Buscar)
boton2.bind('<Button-1>',Seleccion)
boton3.bind('<Button-1>',Agregar)


ventana.mainloop()
