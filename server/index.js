var mysql = require('mysql');
var connection = mysql.createConnection({
    host: 'localhost',
    user: 'test',
    password: '',
    port: 3306,
    database: 'PET_REGIST'
});

/*
    AGENCY_TB_PK: int(11),
    ADDRESS1: varchar(80),
    ADDRESS2: varchar(50),
    CEO_NAME: varchar(10),
    AGENCY_NAME: varchar(20),
    PHONE_NUMBER: varchar(15)
*/

let data = new Array();


// 콜백 promise
connection.connect();

const getDB = function() {
    return new Promise(function(resolve, reject) {
        connection.query('SELECT * from AGENCY_TB', function(err, rows, fields) {
            data = rows;
            // console.log(data[0])
            console.log("read Data !!")
        });
    }).then((result) => {
        connection.end();
    }, (error) => {
        console.err(err)
    })
}


const express = require('express');
const app = express();

app.get('/getData', (req, res) => {
    console.log('who get in here/getData');
    getDB();
    res.json(data);
});

app.listen(3000, () => {
    console.log('Example app listening on port 3000!');
});