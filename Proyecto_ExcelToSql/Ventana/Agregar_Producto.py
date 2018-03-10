#!/usr/bin/env python
# -*- coding: utf-8 -*-

from PIL import ImageTk, Image as IMAG
import pymysql #SQL
import time
from tkinter import *
from tkinter import ttk
from tkinter import messagebox
from tkinter import filedialog
import re
import boto3
from botocore.client import Config
import time

######## SUBIR IMAGEN
def AbreVentana(event):
    global pic
    pic = filedialog.askopenfilename()
    imag.set(pic)
    '''
    img = IMAG.open(pic)
    o_size = img.size  # Tamaño original de la imagen
    f_size = (100, 100)  # Tamaño del canvas donde se mostrará la imagen

    factor = min(float(f_size[1]) / o_size[1], float(f_size[0]) / o_size[0])
    width = int(o_size[0] * factor)
    height = int(o_size[1] * factor)

    rImg = img.resize((width, height), IMAG.ANTIALIAS)
    rImg = ImageTk.PhotoImage(rImg)

    canvas = Canvas(ventAbrir, width=f_size[0], height=f_size[1])
    canvas.create_image(f_size[0] / 2, f_size[1] / 2, anchor=CENTER, image=rImg, tags="img")
    canvas.grid(row=5, column=4)
    '''




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


#####Avisos
def Aviso(texto):
    messagebox.showwarning("Aviso",texto)

#Ventana para agregar producto
def AgregarBD(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    cur.execute("SELECT max(code) FROM mainDataBase.Product ;")
    a = cur.fetchall()[0][0]
    a = int(a) + 1
    codeAbrir.set(str(a))
    fecha_actu.set(time.strftime('%Y-%m-%d %H:%M:%S'))

    code = codeAbrir.get()
    name = nombre.get()
    description = descri.get()
    shortDescription = Descri_Corta.get()
    imgSrc = code +".jpg"
    DateLastUpdate = fecha_actu.get()
    Category_code = categ.get()
    SubCategory_code = Subcateg.get()
    Brand_code = Marca.get()
    BrandForm_code = MarcaForm.get()



    if len(name) > 0 and len(description) > 0 and len(shortDescription) > 0 and len(Category_code)\
            > 0 and len(SubCategory_code) > 0 and len(Brand_code) > 0 and len(BrandForm_code) > 0:
        a = messagebox.askyesno(title="CONFIRMACION", message="¿Esta seguro de los datos a subir?")
        if a == True:
            query = "INSERT INTO  mainDataBase.Product "
            query += "VALUES ('" + code + "','" + name + "','" + description + "','" + shortDescription \
                     + "','" + imgSrc + "','"
            query += DateLastUpdate + "','" + Category_code + "','" + SubCategory_code + "','" + Brand_code \
                     + "','" + BrandForm_code + "');"
            cur.execute(query)
            conn.commit()
            ###Insertar imagen
            SubeImagen(pic, imgSrc)
            codeAbrir.set("")
            nombre.set("")
            descri.set("")
            Descri_Corta.set("")
            imag.set("")
            fecha_actu.set("")
            categ.set("")
            Subcateg.set("")
            Marca.set("")
            MarcaForm.set("")

    else:
        texto="Complete todos los campos"
        Aviso(texto)

    cur.close()
    conn.close()
def BuscCateg(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    category = combo8.get()
    cur.execute("SELECT * FROM mainDataBase.Category WHERE name LIKE '%"+category+"%';")
    valuesCombo = []
    for a in cur.fetchall():
        valuesCombo.append(a[1])

    combo8["values"] = valuesCombo
    combo8.set("")
    categ.set("")

    cur.close()
    conn.close()
def MatchCatego(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    category = combo8.get()
    cur.execute("SELECT * FROM mainDataBase.Category WHERE name = '"+category+"';")
    categ.set(cur.fetchall()[0][0])

    cur.close()
    conn.close()
def BuscSubCateg(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    subcategory = combo9.get()
    cur.execute("SELECT * FROM mainDataBase.SubCategory WHERE name LIKE '%"+subcategory+"%';")
    valuesCombo = []
    for a in cur.fetchall():
        valuesCombo.append(a[1])

    combo9["values"] = valuesCombo
    combo9.set("")
    Subcateg.set("")

    cur.close()
    conn.close()
def MatchSubCatego(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    subcategory = combo9.get()
    cur.execute("SELECT * FROM mainDataBase.SubCategory WHERE name = '"+subcategory+"';")
    Subcateg.set(cur.fetchall()[0][0])#box de codigo

    cur.close()
    conn.close()
def BuscMarca(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    marca = combo10.get()
    cur.execute("SELECT * FROM mainDataBase.Brand WHERE name LIKE '%"+marca+"%';")
    valuesCombo = []
    for a in cur.fetchall():
        valuesCombo.append(a[1])

    combo10["values"] = valuesCombo
    combo10.set("")
    Marca.set("")
    # combo10.state("readonly")
    ##Carga BrandForm
    cur.execute("SELECT * FROM mainDataBase.BrandForm")
    valuesCombo1 = []
    for a in cur.fetchall():
        valuesCombo1.append(a[1])

    combo11["values"] = valuesCombo1

    cur.close()
    conn.close()
def MatchMarca(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    marca = combo10.get()
    cur.execute("SELECT * FROM mainDataBase.Brand WHERE name = '"+marca+"';")
    Marca.set(cur.fetchall()[0][0])#box de codigo

    cur.close()
    conn.close()
def MatchMarcaForm(event):
    conn = pymysql.connect(host='maindb.crmxmotjuru4.us-east-1.rds.amazonaws.com', port=3306, user='admin',
                           passwd='LastOne1*', db='mainDataBase')
    cur = conn.cursor()
    marcaF = combo11.get()
    cur.execute("SELECT * FROM mainDataBase.BrandForm WHERE name = '"+marcaF+"';")
    MarcaForm.set(cur.fetchall()[0][0])#box de codigo

    cur.close()
    conn.close()

################VENTANA
#print(time.strftime('%Y-%m-%d %H:%M:%S'))
ventAbrir = Tk()
ventAbrir.geometry('800x500+200+50')
ventAbrir.title('Agregar producto')


##########Elementos de la ventana Agregar Producto
l1 = Label(ventAbrir, text="Codigo Producto: ")
l1.grid(row=1, column=1, sticky=E)
codeAbrir = StringVar()
caj1 = Entry(ventAbrir, textvariable=codeAbrir, state=DISABLED)
caj1.grid(row=1, column=2,sticky=W)

l2 = Label(ventAbrir, text="Nombre: ")
l2.grid(row=2, column=1, sticky=E)
nombre = StringVar()
caj1 = Entry(ventAbrir, textvariable=nombre)
caj1.grid(row=2, column=2,sticky=W)

l3 = Label(ventAbrir, text="Descripción: ")
l3.grid(row=3, column=1, sticky=E)
descri = StringVar()
caj1 = Entry(ventAbrir,width=40, textvariable=descri)#bg="linen"
caj1.grid(row=3, column=2,sticky=W)#,ipady=2,rowspan=2

l4 = Label(ventAbrir, text="Descripcion Corta: ")
l4.grid(row=4, column=1, sticky=E)
Descri_Corta = StringVar()
caj4 = Entry(ventAbrir, textvariable=Descri_Corta)
caj4.grid(row=4, column=2,sticky=W)

l5 = Label(ventAbrir, text="Imágen: ")
l5.grid(row=5, column=1, sticky=E)
imag = StringVar()
caj5 = Entry(ventAbrir, textvariable=imag,state="readonly")
caj5.grid(row=5, column=2,sticky=W)
bot5 = Button(ventAbrir, text="AbrirImag")
bot5.grid(row=5, column=3)
bot5.bind('<Button-1>', AbreVentana)

l6 = Label(ventAbrir, text="Fecha de actualización: ")
l6.grid(row=6, column=1, sticky=E)
fecha_actu = StringVar()
caj7 = Entry(ventAbrir, textvariable=fecha_actu,state=DISABLED)
caj7.grid(row=6, column=2,sticky=W)

#############Category
l8 = Label(ventAbrir, text="Categoria: ")
l8.grid(row=7, column=1, sticky=E)
combo8 = ttk.Combobox(width=24)#state="readonly"##para que el usuario no ingrese sus valores
combo8.grid(row=7,column=2,sticky=E)
bot8 = Button(ventAbrir, text="BuscCateg")
bot8.grid(row=7, column=3)
bot8.bind('<Button-1>', BuscCateg)
categ = StringVar()
caj8 = Entry(ventAbrir,width=11, textvariable=categ,state=DISABLED)
caj8.grid(row=7, column=2,sticky=W)
combo8.bind('<<ComboboxSelected>>',MatchCatego)

#############SubCategory
l9 = Label(ventAbrir, text="Sub Categoria: ")
l9.grid(row=8, column=1, sticky=E)
combo9 = ttk.Combobox(width=24)#state="readonly"##para que el usuario no ingrese sus valores
combo9.grid(row=8,column=2,sticky=E)
bot9 = Button(ventAbrir, text="BuscSubCateg")
bot9.grid(row=8, column=3)
bot9.bind('<Button-1>', BuscSubCateg)
Subcateg = StringVar()
caj9 = Entry(ventAbrir,width=11, textvariable=Subcateg,state=DISABLED)
caj9.grid(row=8, column=2,sticky=W)
combo9.bind('<<ComboboxSelected>>',MatchSubCatego)

#############Brand
n=9
l10 = Label(ventAbrir, text="Marca: ")
l10.grid(row=n, column=1, sticky=E)
combo10 = ttk.Combobox(width=24)#state="readonly"##para que el usuario no ingrese sus valores
combo10.grid(row=n,column=2,sticky=E)
bot10 = Button(ventAbrir, text="BuscMarca")
bot10.grid(row=n, column=3)
bot10.bind('<Button-1>', BuscMarca)
Marca = StringVar()
caj10 = Entry(ventAbrir,width=11, textvariable=Marca,state=DISABLED)
caj10.grid(row=n, column=2,sticky=W)
combo10.bind('<<ComboboxSelected>>',MatchMarca)

#############BrandForm
n = 10
l11 = Label(ventAbrir, text="Marca Form: ")
l11.grid(row=n, column=1, sticky=E)
combo11 = ttk.Combobox(width=24,state="readonly")#state="readonly"##para que el usuario no ingrese sus valores
combo11.grid(row=n,column=2,sticky=E)
MarcaForm = StringVar()
caj11 = Entry(ventAbrir,width=11, textvariable=MarcaForm,state=DISABLED)
caj11.grid(row=n, column=2,sticky=W)
combo11.bind('<<ComboboxSelected>>',MatchMarcaForm)

'''

'''


bot1 = Button(ventAbrir, text="Agregar")
bot1.grid(row=1, column=4)
bot1.bind('<Button-1>', AgregarBD)

ventAbrir.mainloop()


def ejecutaScript():
    #Aquí agregas en contenido de tu script o ejecutas el script dentro de un archivo .py.
    #python myscript.py
    if len(imag.get()) > 0:
        print("mostrando")
        img = IMAG.open(imag.get())
        o_size = img.size  # Tamaño original de la imagen
        f_size = (100, 100)  # Tamaño del canvas donde se mostrará la imagen
        factor = min(float(f_size[1]) / o_size[1], float(f_size[0]) / o_size[0])
        width = int(o_size[0] * factor)
        height = int(o_size[1] * factor)
        rImg = img.resize((width, height), IMAG.ANTIALIAS)
        rImg = ImageTk.PhotoImage(rImg)
        canvas = Canvas(ventAbrir, width=f_size[0], height=f_size[1])
        canvas.create_image(f_size[0] / 2, f_size[1] / 2, anchor=CENTER, image=rImg, tags="img")
        canvas.grid(row=5, column=4)
    print('Ejecutando Script...')
    time.sleep(1)
'''
while True:
    ejecutaScript()
'''
