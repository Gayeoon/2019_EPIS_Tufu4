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

let rett = {
    result: 0
};

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
});

app.post('/getIdCheck', (req, res, next) => {
    console.log('\n\nCALL getIdCheck');
    getIdCheck(req.body);
    res.json(rett);
});

app.post('/getJoin', (req, res, next) => {
    console.log('\n\nCALL getJoin');
    getJoin(req.body);
    res.json(rett);
});

app.post('/getLogin', (req, res, next) => {
    console.log('\n\nCALL getLogin');
    /* #####TEST##### */
    const ret = getLogin(req.body);
    let tt = {
        "result": 999
    };
    ret.then(function(value) {
        tt.result = value;
    })

    res.json(rett);
});

app.post('/getHospitalName', (req, res, next) => {
    console.log('\n\nCALL getHospitalName');
    getHospitalName(req.body);
    res.json(rett);
});

app.post('/getHospitalData', (req, res, next) => {
    console.log('\n\nCALL getHospitalData');
    getHospitalData(req.body);
    res.json(rett);
});

app.get('/updateDB', (req, res, next) => {
    console.log('\n\nCALL GET updateDB');
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
    console.log('\n\nCALL POST updateDB');
    const resData = {};
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

const getIdCheck = async(payload) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        try {
            let query = `SELECT ID from USER_TB where ID = '${payload.user.id}';`

            await connection.query(query, function(err, rows, fields) {
                if (rows.length) {
                    rett.result = 1;
                } else {
                    rett.result = 2;
                }
                console.log(`${payload.user.id}가 중복됩니다`)
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

const getJoin = async(payload) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = payload.user;
        /*
           {"user":{
               "hospital":"힐링힐스동물병원", 
               "name":"박성민", 
               "number":"031-708-0078", 
               "id":"test", 
               "pw":"1234"
            }}
        */
        try {
            let query = `
                INSERT INTO USER_TB (ID, PW, HOSPITAL_KEY, HOSPITAL_NAME) 
                SELECT '${user.id}', '${user.pw}', HOSPITAL_KEY, HOSPITAL_NAME
                from HOSPITALINFO_TB 
                where HOSPITAL_NAME = '${user.hospital}' AND PHONE_NUMBER = '${user.number}';`
            await connection.query(query);
            rett.result = 1; // 성공
            connection.release(); // db 연결 끝
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            rett.result = 0; // 실패
            connection.release();
            return false;
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
}

const getLogin = async(payload) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        try {
            let query = `SELECT HOSPITAL_KEY from USER_TB where ID = '${payload.user.id}' AND PW = '${payload.user.pw}';`
            console.log(payload)
            console.log(payload.user.id, payload.user.pw)

            await connection.query(query, function(err, rows, fields) {
                if (rows.length) {
                    rett.result = 1;
                } else {
                    rett.result = 0;
                }
                console.log("length : " + rows.length)
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

const getHospitalName = async(payload) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        try {
            let query = `SELECT HOSPITAL_NAME from USER_TB where ID = '${payload.user.id}';`
            await connection.query(query, function(err, rows, fields) {
                rett.result = rows;
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

            // console.log(query)
            await connection.query(query, function(err, rows, fields) {
                // console.log("ROWs : " + rows)
                rett.result = rows;
                console.log(rett)
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

const updateHospitalData = async(payload) => {
    try {
        const connection = await pool.getConnection(async conn => conn);
        const newData = payload.Sheet1;
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