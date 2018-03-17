
-- INSERT INTO  mainDataBase.Address(adressName,addressN,latitude,longitude,district,city)
-- VALUES ('jr.pucallpa','151',24.8,25.8,'Brena','Lima');
-- INSERT INTO  mainDataBase.Cost(idCost,cost,frequencyDays,lastPay,nextPay)
-- VALUES ('12',21.3,4,'2018-02-05 13:44:00','2019-02-05 13:44:00');
-- SELECT * FROM mainDataBase.Cost;
-- INSERT INTO  mainDataBase.Cost(idCost,cost,frequencyDays,lastPay,nextPay)
-- VALUES ('11',21.3,4,'2018-02-05 13:44:00','2019-02-05 13:44:00');
-- DELETE FROM mainDataBase.Product
-- WHERE code="1000014998";
-- update mainDataBase.Cost
-- set cost=25.6, frequencyDays=5,lastPay='2018-02-06 13:44:00',nextPay='2019-02-06 13:44:00'
-- where idCost=12;
-- SELECT * FROM mainDataBase.Brand;
 SELECT * FROM mainDataBase.Product
 where code=(SELECT MAX(code) FROM mainDataBase.Product);