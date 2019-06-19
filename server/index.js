const express = require('express');
const bodyParser = require('body-parser');
const multiparty = require('multiparty');
const xlsx = require('xlsx');

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    limit: '150mb',
    extended: false
}));



const mysql = require('mysql2/promise')
const pool = mysql.createPool({
        host: 'localhost',
        user: 'test',
        password: '',
        port: 3306,
        database: 'PET_REGIST'
    })
    // var mysql = require('mysql');
    // var connection = mysql.createConnection({
    //     host: 'localhost',
    //     user: 'test',
    //     password: '',
    //     port: 3306,
    //     database: 'PET_REGIST'
    // });

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
const resData = {}; // db update 할 때 사용

app.set('port', process.env.PORT || 3000);

app.listen(3000, () => {
    console.log('Example app listening on port 3000!');
});

/*
 * route
 */

app.get('/', (req, res, next) => {
    const str = 'Hi ! I\'m here !\n\n'
    console.log(str);

    res.send(str);
    // res.status(404).send('미아냉 404야 ㅠㅠ')
});
app.get('/isLogon', (req, res, next) => {
    const str = 'Hi ! I\'m here !\n\n'
    console.log(str);

    res.send(str);
    // res.status(404).send('미아냉 404야 ㅠㅠ')
});

app.post('/getHospitalData', (req, res, next) => {
    console.log('CALL getHospitalData\n\n');

    console.log(req.body);
    getHospitalData(req.body);
    res.json(data);
});

app.get('/updateDB', (req, res, next) => {
    let contents = '';
    contents += '<html><body>';
    contents += '   <form action="/updateDB" method="POST" enctype="multipart/form-data">';
    contents += '       <input type="file" name="xlsx" />';
    contents += '       <input type="submit" />';
    contents += '   </form>';
    contents += '</body></html>';

    res.send(contents);
});

app.post('/updateDB', (req, res, next) => {
    // const resData = {};
    const form = new multiparty.Form({
        autoFiles: true, // POST만 가능하도록
    });

    form.on('file', (name, file) => {
        const workbook = xlsx.readFile(file.path);
        const sheetnames = Object.keys(workbook.Sheets);

        let i = sheetnames.length;

        // xlsx -> json
        while (i--) {
            const sheetname = sheetnames[i];
            resData[sheetname] = xlsx.utils.sheet_to_json(workbook.Sheets[sheetname]);
        }
    });


    form.on('close', () => {
        res.send(resData);
        updateHospitalData(resData);
    });

    form.parse(req);

});

/* 
 * database 
 * 
 * async, await 필수 -> Promise 는 오류뜸
 */

const getHospitalData = async(payload) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        try {
            let query;

            // 병원명이나 대표자명에 searchword가 포함된 col을 찾아서 보낸다.
            if (payload.searchword == 'allHospitalData')
                query = `SELECT * from HOSPITALINFO_TB`
            else
                query = `SELECT CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2, SIGNUP_APP from HOSPITALINFO_TB
                WHERE CEO_NAME LIKE '%${payload.searchword}%' OR HOSPITAL_NAME LIKE '%${payload.searchword}%';`

            console.log(query)
            await connection.query(query, function(err, rows, fields) {
                console.log("ROWs : " + rows)
                data.result = rows;
            });
            connection.release(); // db 연결 끝
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            return false;
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
}

/* get connection */
const updateHospitalData = async(data) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        const newData = data.Sheet1;
        let tmpstr = [''], // 너무 기니까 1000개씩 나눠서 담자
            query = '',
            i = 0;
        try {
            Array.prototype.forEach.call(newData, (el, idx) => {
                if (idx / 1000 == i + 1) {
                    tmpstr.push('');
                    i++; // 1000개씩 나누어 담는다 
                }
                tmpstr[i] += `('${idx+1}', '${el.대표자명}', '${el.업체명}', '${el.업체전화번호}', '${el.주소}', '${el.상세주소}'),`
            });

            // DB에 삽입하는 쿼리문
            query = `INSERT INTO HOSPITALINFO_TB (HOSPITAL_KEY, CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2) 
            VALUES `

            await connection.query('SET FOREIGN_KEY_CHECKS = 0;') // foreign key 무시
            await connection.query('truncate HOSPITALINFO_TB;') // table 비운다
                // table 새로 채우자
            while (i >= 0) {
                tmpstr[i].trim();
                tmpstr[i] = tmpstr[i].substring(0, tmpstr[i].length - 1); // 마지막 ',' 없애주기
                const [rows] = await connection.query(query + tmpstr[i] + ';')
                    // console.log(rows)
                i--;
            }
            await connection.query('SET FOREIGN_KEY_CHECKS = 1;') // foreign key 다시 체크
            connection.release(); // db 연결 끊는다
            // return rows;

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            return false;
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
};