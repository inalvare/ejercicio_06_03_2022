INSERT INTO marcas (nombre) VALUES('Onda');
INSERT INTO marcas (nombre) VALUES('Toyota');

INSERT INTO modelos (nombre) VALUES('el primero');
INSERT INTO modelos (nombre) VALUES('el segundo');

INSERT INTO coches (marca, modelo, motor, cilindrada , velocidad) VALUES(2,"1,"ascd21",300,100);
INSERT INTO coches (marca, modelo, motor, cilindrada , velocidad) VALUES(1,"2,"dskjdk32",300,100);
INSERT INTO coches (marca, modelo, motor, cilindrada , velocidad) VALUES(1,2,"rkj24ms",300,100);
INSERT INTO coches (marca, modelo, motor, cilindrada , velocidad) VALUES(2,1,"fhi23nfc",300,100);

INSERT INTO `usuarios` (username,password,enabled) VALUES('iñigo','$2a$10$Jf1B1DvYy3spSruEe8kf4OXx1jeyPaOgTHPgXiUaUQQ/s/O.PWhbu',1);
INSERT INTO `usuarios` (username,password,enabled) VALUES('admin','$2a$10$8t2e9DEl.ZSajFHzwu/JKexkpmgoIpH6JQsK.rWlseVjAxCCzuf/K',1);

INSERT INTO `roles` (nombre) VALUES('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id,role_id) VALUES(1,1);
INSERT INTO `usuarios_roles` (usuario_id,role_id) VALUES(2,2);
INSERT INTO `usuarios_roles` (usuario_id,role_id) VALUES(2,1);

