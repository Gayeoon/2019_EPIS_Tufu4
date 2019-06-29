const express = require('express');
const bodyParser = require('body-parser');
const multiparty = require('multiparty');
const xlsx = require('xlsx');
const fs = require('fs');

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    limit: '150mb',
    extended: false
}));

const profile_file_path = './image/';

const mysql = require('mysql2/promise')
const pool = mysql.createPool({
    host: 'localhost',
    user: 'test',
    password: '',
    port: 3306,
    database: 'PET_REGIST'
})


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


/*
 * 병원
 */

app.post('/getIdCheck', async(req, res, next) => {
    console.log('\n\nCALL getIdCheck');
    /*
        {"user": {"id": "test"}}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `SELECT ID from USER_TB where ID = '${user.id}';`;

            await connection.query(query, function(err, rows, fields) {
                if (rows.length) {
                    ret.result = 1;
                    console.log(`${user.id}가 중복됩니다`)
                } else {
                    ret.result = 2;
                    console.log(`${user.id}가 중복되지 않습니다`)
                }
                connection.release(); // db 연결 끝
                res.json(ret);
            });

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
});

app.post('/getJoin', async(req, res, next) => {
    console.log('\n\nCALL getJoin');
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
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: "init" };
        try {
            let query = `
                INSERT INTO USER_TB (ID, PW, HOSPITAL_KEY, HOSPITAL_NAME) 
                SELECT '${user.id}', '${user.pw}', HOSPITAL_KEY, HOSPITAL_NAME
                from HOSPITALINFO_TB 
                where PHONE_NUMBER = '${user.number}';`
                // where HOSPITAL_NAME = '${user.hospital}' AND PHONE_NUMBER = '${user.number}';`
            await connection.query(query, function(err, result) {
                console.log("connection query")
                try {
                    console.log(result.affectedRows)
                    if (result.affectedRows != 0) { // 잘 들어갔다
                        ret.result = 1; // 성공
                        console.log(`가입한 아이디 : ${user.id}`)
                        query = `UPDATE HospitalInfo_TB SET SIGNUP_APP = '1' WHERE HOSPITAL_KEY = (
                            SELECT HOSPITAL_KEY from USER_TB where ID = '${user.id}');`
                        try {
                            connection.query(query, function(err, result) {
                                console.log("connection query")
                                if (result.affectedRows != 0) // 잘 들어갔다
                                    ret.result = 1; // 성공
                                else ret.result = 0; // 실패
                            });
                        } catch (err) {
                            console.error(err)
                            ret.result = 0; // 실패
                        }

                    } else {
                        ret.result = 0; // 실패
                    }
                    connection.release(); // db 연결 끝
                    res.json(ret)
                } catch (err) {
                    console.error("\tERROR : \n" + err)
                    ret.result = 0; // 실패
                    connection.release(); // db 연결 끝
                    res.json(ret)
                }
            });
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            ret.result = 0; // 실패
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getLogin', async(req, res, next) => {
    console.log('\n\nCALL getLogin');
    /*
        {"user": {
            "id": "test",
            "pw": "0000"
        }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: "init" };
        try {
            let query = `SELECT HOSPITAL_KEY from USER_TB where ID = '${user.id}' AND PW = '${user.pw}';`
            console.log(user.id, user.pw)

            await connection.query(query, function(err, rows, fields) {
                if (rows.length) {
                    ret.result = 1;
                } else {
                    ret.result = 0;
                }
                console.log("length : " + rows.length)
                connection.release(); // db 연결 끝
                res.json(ret)
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(null)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/putProfile', async(req, res, next) => {
    console.log('\n\nCALL putProfile\n');
    /*
        {"user": {
            "id": "test",
            "profile": ""
        }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            console.log("LENGTH ::::: " + user.profile.length)
            user.id = user.id.trim();
            const file_path = `${profile_file_path}${user.id}.bmp`
            let query = `UPDATE USER_TB SET PROFILE = '${file_path}' where id = '${user.id}';`

            const bitmap = Buffer.from(user.profile, 'base64');
            fs.writeFileSync(file_path, bitmap);
            console.log('******** base64로 인코딩되었던 파일 쓰기 성공 ********');

            await connection.query(query, function(err, rows, fields) {
                console.log(rows)
                if (rows != null) { // auto commit ok 잘 들어갔다
                    // if (rows.affectedRows != 0) { // auto commit ok 잘 들어갔다
                    ret.result = 1;
                } else {
                    ret.result = 0;
                }
                connection.release(); // db 연결 끝
                res.json(ret)
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(null)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getThreeCheck', async(req, res, next) => {
    console.log('\n\nCALL getThreeCheck');
    /*
        {"user":{
            "hospital":"미리내동물병원",
            "name":"김가연",
            "number":"031-574-7580"
        }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `SELECT HOSPITAL_KEY from HOSPITALINFO_TB where CEO_NAME = '${user.name}' AND PHONE_NUMBER = '${user.number}';`
            await connection.query(query, function(err, rows, fields) {
                if (rows.length) {
                    ret.result = 1;
                } else {
                    ret.result = 2;
                }
                console.log("length : " + rows.length)
                connection.release(); // db 연결 끝
                res.json(ret)
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getHospitalData', async(req, res, next) => {
    console.log('\n\nCALL getHospitalData');
    /*
        {"user": {"id": "test"}}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = {
            result: {
                name: "",
                new: 0,
                wait1: 0,
                wait2: 0,
                finish: 0,
                profile: ""
            }
        };
        try {
            user.id = user.id.trim();
            const file_path = `${profile_file_path}${user.id}.bmp`

            let query = `SELECT HOSPITAL_NAME as 'hospital_name', REGIST_STATE as 'regist_state', COUNT(*) as 'count' 
            from RESERVATION_TB as A join USER_TB as B 
            on A.HOSPITAL_KEY = B.HOSPITAL_KEY AND B.ID = '${user.id}'
            GROUP BY REGIST_STATE;`

            await connection.query(query, function(err, rows, fields) {
                console.log("SSSSSSS")
                console.log(rows)

                const bitmap = fs.readFileSync(file_path);
                const profile = Buffer.from(bitmap).toString('base64');
                ret.result.profile = profile;

                if (rows.length) {
                    ret.result.name = rows[0].hospital_name;
                    Array.prototype.forEach.call(rows, (el, idx) => {
                        switch (el.regist_state) {
                            case 1:
                                ret.result.new = el.count;
                                break;
                            case 2:
                                ret.result.wait1 = el.count;
                                break;
                            case 3:
                                ret.result.wait2 = el.count;
                                break;
                            case 4:
                                ret.result.finish = el.count;
                                break;
                            default:
                                ret.result.name = "there is no hospital";
                                break;
                        }
                    });
                    console.log(`get ${ret.result.name} of regist_state`)
                        // console.log(ret)
                    connection.release(); // db 연결 끝
                    res.json(ret)
                } else {
                    try {
                        let query = `SELECT HOSPITAL_NAME from USER_TB where ID = '${user.id}';`
                        connection.query(query, function(err, rows, fields) {
                            console.log(rows[0].HOSPITAL_NAME)
                            ret.result.name = rows[0].HOSPITAL_NAME;
                            connection.release(); // db 연결 끝
                            res.json(ret)
                        });
                    } catch (err) {
                        console.error(err);
                        connection.release(); // db 연결 끝
                        res.json(ret)
                    }
                }
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getNewtReservationListData', async(req, res, next) => {
    console.log('\n\nCALL getNewtReservationListData');
    /*
        {"user": {
            "id": "test",
            "state": "1"
        }}

        type - > 1: 내장형 / 2: 외장형 / 3: 등록인식표
    */

    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = {
            result: {
                internal: [],
                external: [],
                dogtag: []
            }
        };
        try {
            let query = `SELECT OWNER_NAME, ASK_DATE, TYPE from RESERVATION_TB where ID = '${user.id}'
            AND REGIST_STATE = '${user.state}';`
            await connection.query(query, function(err, rows, fields) {
                console.log(rows)
                const arr = rows;
                const tmp = { OWNER_NAME: 'owner', ASK_DATE: '2000-01-01' };
                Array.prototype.forEach.call(arr, (el, idx) => {
                    console.log(el.TYPE)
                    switch (el.TYPE) {
                        case 1: // 내장형
                            tmp.OWNER_NAME = el.OWNER_NAME;
                            tmp.ASK_DATE = el.ASK_DATE;
                            ret.result.internal.push(tmp);
                            break;
                        case 2: // 외장형
                            tmp.OWNER_NAME = el.OWNER_NAME;
                            tmp.ASK_DATE = el.ASK_DATE;
                            ret.result.external.push(tmp);
                            break;
                        case 3: // 등록인식표
                            tmp.OWNER_NAME = el.OWNER_NAME;
                            tmp.ASK_DATE = el.ASK_DATE;
                            ret.result.dogtag.push(tmp);
                            break;
                    }
                })
                connection.release();
                res.json(ret)
            });
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }

    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getReservationData', async(req, res, next) => {
    console.log('\n\nCALL getReservationData');
    /*
        {"user": {
            "id": "test",
            "owner_name": "김가연",
            "type": "1"
        }}
    */

    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `SELECT 
        OWNER_NAME,OWNER_RESIDENT,OWNER_PHONE_NUMBER,OWNER_ADDRESS1,OWNER_ADDRESS2,
        PET_NAME,PET_VARIETY,PET_COLOR,PET_GENDER,PET_NEUTRALIZATION,PET_BIRTH,
        ASK_DATE,ETC
        from RESERVATION_TB where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND TYPE = '${user.type}';`
            await connection.query(query, function(err, rows, fields) {
                ret.result = rows
                connection.release(); // db 연결 끝
                res.json(ret)
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/putChangeState', async(req, res, next) => {
    console.log('\n\nCALL putChangeState');
    /*
        {"user": {
            "id": "test",
            "owner_name": "김가연",
            "type": "1"
        }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `UPDATE RESERVATION_TB SET REGIST_STATE = '2' 
            where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND TYPE = '${user.type}';`

            await connection.query(query, function(err, reuslt) {
                if (result.affectedRows) { // DB의 값이 변경된 것이 있다면
                    ret.result = 1;
                } else {
                    ret.result = 0;
                }
                connection.release(); // db 연결 끝
                res.json(ret)
            });
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/putChangeWait', async(req, res, next) => {
    console.log('\n\nCALL putChangeWait');
    /*
        {"user": {
            "id": "test",
            "owner_name": "김가연",
            "pet_name": "뿡이"
        }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `SELECT OWNER_PHONE_NUMBER from RESERVATION_TB 
            where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND PET_NAME = '${user.pet_name}';`

            await connection.query(query, function(err, rows, fields) {
                ret.result = rows;
                query = `UPDATE RESERVATION_TB SET REGIST_STATE = '3' 
                where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND PET_NAME = '${user.pet_name}';`

                try {
                    connection.query(query, function(err, result) {
                        if (!result.affectedRows) { // DB의 값이 아무것도 변경된 것이 없다면
                            ret.result = 0;
                        }
                        connection.release(); // db 연결 끝
                        res.json(ret)
                    });
                } catch (err) {
                    console.log('Query Error\n\n');
                    console.log(err);
                    connection.release();
                    res.json(ret)
                }
            });
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getWaitReservationListData', async(req, res, next) => {
    console.log('\n\nCALL getWaitReservationListData');
    /*
        {"user": { "id": "test" }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: { wait: [] } };
        try {
            let query = `SELECT OWNER_NAME, PET_NAME
            from RESERVATION_TB where REGIST_STATE = '2' OR REGIST_STATE = '3';`
            await connection.query(query, function(err, rows, fields) {
                ret.result.wait = rows
                connection.release(); // db 연결 끝
                res.json(ret)
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/getReservationInfoData', async(req, res, next) => {
    console.log('\n\nCALL getReservationInfoData');
    /*
        {"user": {
            "id": "test",
            "owner_name": "김가연",
            "pet_name": "뿡이"
        }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `SELECT 
            OWNER_NAME,OWNER_RESIDENT,OWNER_PHONE_NUMBER,OWNER_ADDRESS1,OWNER_ADDRESS2,
            PET_NAME,PET_VARIETY,PET_COLOR,PET_GENDER,PET_NEUTRALIZATION,PET_BIRTH,
            ASK_DATE,ETC
            from RESERVATION_TB where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND PET_NAME = '${user.pet_name}';`
            await connection.query(query, function(err, rows, fields) {
                ret.result = rows
                connection.release(); // db 연결 끝
                res.json(ret)
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});


/*
 * 병원
 */

app.post('/searchHospitalData', async(req, res, next) => {
    console.log('\n\nCALL searchHospitalData');
    /*
        {"searchword": "힐링동물병원"}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const searchword = req.body.searchword;
        const ret = { result: 0 };
        try {
            let query;

            console.log(`searchword : ${searchword}`)

            // 병원명이나 대표자명에 searchword가 포함된 col을 찾아서 보낸다.
            if (searchword == 'allHospitalData')
                query = `SELECT * from HOSPITALINFO_TB`
            else
                query = `SELECT HOSPITAL_KEY, CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2, SIGNUP_APP from HOSPITALINFO_TB
            WHERE CEO_NAME LIKE '%${searchword}%' OR HOSPITAL_NAME LIKE '%${searchword}%';`

            // console.log(query)
            await connection.query(query, function(err, rows, fields) {
                // console.log("ROWs : " + rows)
                ret.result = rows;
                console.log(ret)
                connection.release(); // db 연결 끝
                res.json(ret)
            });
        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

app.post('/sendMessage', async(req, res, next) => {
    console.log('\n\nCALL sendMessage');
    /*
        {
            "key": "hospital key",
            "type": "1 / 2" -> 1 : 내장형, 2 : 외장형,
            "ownerName": "",
            "address": "",
            "phone_number": "",
            "petName": "",
            "race": "",
            "petColor": "",
            "petBirth": "",
            "neutralization": "",
            "petGender": "",
        }

        REGIST_STATE
            신규예약    :    1
            등록대기-1    :    2
            등록대기-2    :    3
            등록완료    :    4
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const msg = req.body
        const ret = { result: 0 };
        try {
            let query = `INSERT INTO RESERVATION_TB 
                ('ID', 
                'HOSPITAL_KEY', 'TYPE', 'OWNER_NAME', 'ADDRESS', 'PHONE_NUMBER', 'PET_NAME', 
                'RACE', 'PET_COLOR', 'PET_BIRTH', 'NEUTRALIZATION', 'PET_GENDER', 'REGIST_STATE') 
            VALUES 
                ( (SELECT ID from USER_TB where HOSPITAL_KEY = '${msg.key}'), 
                '${msg.key}', '${msg.type}', '${msg.ownerName}', '${msg.address}', '${msg.phone_number}', '${msg.petName}', 
                '${msg.race}', '${msg.petColor}', '${msg.petBirth}', '${msg.neutralization}', '${msg.petGender}', '1');`

            await connection.query(query, function(err, result) {
                console.log("connection query")
                try {
                    console.log(result.affectedRows)
                    if (result.affectedRows != 0) { // 잘 들어갔다
                        ret.result = 1; // 성공
                    } else {
                        ret.result = err; // 실패
                    }
                    connection.release(); // db 연결 끝
                    res.json(ret)
                } catch (err) {
                    console.error("\tERROR : \n" + err)
                    ret.result = err; // 실패
                    connection.release(); // db 연결 끝
                    res.json(ret)
                }
            });

        } catch (err) {
            console.log('Query Error\n\n');
            console.log(err);
            connection.release();
            res.json(ret)
        }
    } catch (err) {
        console.log('DB Error');
        return false;
    }
});

/*
 * DB 
 */

app.get('/updateHospitalData', (req, res, next) => {
    console.log('\n\nCALL GET updateHospitalData');
    let contents = '';
    contents += '<html><body>';
    contents += '   <form action="/updateHospitalData" method="POST" enctype="multipart/form-data">';
    contents += '       <input type="file" name="xlsx" />';
    contents += '       <input type="submit" />';
    contents += '   </form>';
    contents += '</body></html>';

    res.send(contents);
});

app.post('/updateHospitalData', (req, res, next) => {
    console.log('\n\nCALL POST updateHospitalData');
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
}