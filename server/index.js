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
    - HospitalInfo_TB
    
    HOSPITAL_KEY: int   -> PK
    CEO_NAME: varchar(10) NOT NULL
    HOSPITAL_NAME: varchar(20) NOT NULL
    PHONE_NUMBER: varchar(15) DEFAULT NULL
    ADDRESS1: varchar(80) DEFAULT NULL
    ADDRESS2: varchar(50) DEFAULT NULL
    SIGNUP_APP: tinyint(1) NOT NULL DEFAULT 0

    - USER_TB
    
    ID: varchar(20)   -> PK
    PW: varchar(30) NOT NULL
    HOSPITAL_KEY: varchar(20) NOT NULL
    
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
            query = `SELECT * from HOSPITALINFO_TB`
        else
            query = `SELECT CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2, SIGNUP_APP from HOSPITALINFO_TB
            WHERE CEO_NAME LIKE '${payload.searchword}' OR HOSPITAL_NAME LIKE '${payload.searchword}'`

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