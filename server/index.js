var mysql = require('mysql');
var connection = mysql.createConnection({
    host: 'localhost',
    user: 'test',
    password: '',
    port: 3306,
    database: 'PET_REGIST'
});

const express = require('express');
const app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.json());

/*
    ID: varchar(20)
    PW: varchar(30)
    CEO_NAME: varchar(10)
    AGENCY_NAME: varchar(20)
    PHONE_NUMBER: varchar(15)
    ADDRESS1: varchar(80)
    ADDRESS2: varchar(50)
    SIGNUP_APP: tinyint(1)      tinyint(boolean)
*/

let data = {
    result: null
};


app.listen(3000, () => {
    console.log('Example app listening on port 3000!');
});

app.post('/getHospitalData', (req, res, next) => {
    console.log('CALL getHospitalData\n\n');

    console.log(req.body);

    getHospitalData(req.body);
    res.json(data);
});


connection.connect();
const getHospitalData = function(payload) {
    return new Promise(function(resolve, reject) {
        let query;

        if (payload.searchword == 'allHospitalData')
            query = `SELECT * from AGENCY_TB`
        else
            query = `SELECT CEO_NAME, AGENCY_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2, SIGNUP_APP from AGENCY_TB
            WHERE CEO_NAME LIKE '${payload.searchword}' OR AGENCY_NAME LIKE '${payload.searchword}'`

        connection.query(query, function(err, rows, fields) {
            data.result = rows;
            console.log(payload)
        });
    }).then(() => {
        connection.end();
    }, (err) => {
        console.err(err)
    })
}