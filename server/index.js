const express = require('express');
const bodyParser = require('body-parser');
const multiparty = require('multiparty');
const xlsx = require('xlsx');
const fs = require('fs');
const mysql = require('mysql2/promise')

const app = express();

app.use(bodyParser.json({ limit: '50mb' }));
app.use(bodyParser.urlencoded({
    limit: '150mb',
    extended: true
}));

const profile_file_path = './image/profile/';
const article_file_path = './image/article/';

const pool = mysql.createPool({
    host: 'nodejs-003.cafe24.com',
    // host: '10.0.0.1',
    user: 'poiu8944',
    password: 'admin2019!',
    port: 3306,
    database: 'poiu8944'
})

// const pool = mysql.createPool({
//     host: 'localhost',
//     user: 'test',
//     password: '',
//     port: 3001,
//     database: 'PET_REGIST'
// })


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
                from HospitalInfo_TB 
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
            let query = `UPDATE USER_TB SET PROFILE_URL = '${file_path}' where id = '${user.id}';`

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
            let query = `SELECT HOSPITAL_KEY from HospitalInfo_TB where CEO_NAME = '${user.name}' AND PHONE_NUMBER = '${user.number}';`
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

app.post('/getSearchListData', async(req, res, next) => {
    console.log('\n\nCALL getSearchListData');
    /*
        {"user": { 
            "id": "test",
            "key": "뿡이"
        }}

        {
            "result": {
                "state": [
                    {
                        "OWNER_NAME": "김가연",
                        "PET_NAME": "뿡이",
                        "REGIST_STATE": 1
                    },
                    {
                        "OWNER_NAME": "정지원",
                        "PET_NAME": "맥북",
                        "REGIST_STATE": 2
                    }
                ]
            }
        }

    */

    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = {
            result: {
                state: []
            }
        };
        try {
            let query = `select OWNER_NAME, PET_NAME, REGIST_STATE from RESERVATION_TB 
            WHERE (OWNER_NAME LIKE '%${user.key}%' OR PET_NAME LIKE '%${user.key}%')
            AND ID = '${user.id}';`
            await connection.query(query, function(err, rows, fields) {
                console.log(rows)
                ret.result.state = rows;
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

app.post('/getStateListData', async(req, res, next) => {
    console.log('\n\nCALL getStateListData');
    /*
        {"user": { "id": "test" }}

        {
            "result": {
                "state": [
                    {
                        "OWNER_NAME": "김가연",
                        "PET_NAME": "뿡이",
                        "REGIST_STATE": 1
                    },
                    {
                        "OWNER_NAME": "정지원",
                        "PET_NAME": "맥북",
                        "REGIST_STATE": 2
                    }
                ]
            }
        }

    */

    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = {
            result: {
                state: []
            }
        };
        try {
            let query = `select OWNER_NAME, PET_NAME, REGIST_STATE from RESERVATION_TB where ID = '${user.id}';`
            await connection.query(query, function(err, rows, fields) {
                console.log(rows)
                ret.result.state = rows;
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
                            ret.result.internal.push({ OWNER_NAME: el.OWNER_NAME, ASK_DATE: el.ASK_DATE });
                            break;
                        case 2: // 외장형
                            tmp.OWNER_NAME = el.OWNER_NAME;
                            tmp.ASK_DATE = el.ASK_DATE;
                            ret.result.external.push({ OWNER_NAME: el.OWNER_NAME, ASK_DATE: el.ASK_DATE });
                            break;
                        case 3: // 등록인식표
                            tmp.OWNER_NAME = el.OWNER_NAME;
                            tmp.ASK_DATE = el.ASK_DATE;
                            ret.result.dogtag.push({ OWNER_NAME: el.OWNER_NAME, ASK_DATE: el.ASK_DATE });
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
                // console.log(rows)
                // console.log(rows[0])
                ret.result = rows[0]
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
            console.log(user)
            let query = `UPDATE RESERVATION_TB SET REGIST_STATE = '2' 
            where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND TYPE = '${user.type}';`

            await connection.query(query, function(err, result) {
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
            console.log(user)
            let query = `SELECT OWNER_PHONE_NUMBER from RESERVATION_TB 
            where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND PET_NAME = '${user.pet_name}';`

            await connection.query(query, function(err, rows, fields) {
                ret.result = rows[0];
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

app.post('/putCancelReservation', async(req, res, next) => {
    console.log('\n\nCALL putCancelReservation');
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
            let query = `UPDATE RESERVATION_TB SET REGIST_STATE = '4' 
            where ID = '${user.id}' AND OWNER_NAME = '${user.owner_name}' AND PET_NAME = '${user.pet_name}';`

            await connection.query(query, function(err, result) {
                if (result.affectedRows) { // DB의 값이 아무것도 변경된 것이 없다면
                    ret.result = 1;
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

app.post('/getFinishReservationListData', async(req, res, next) => {
    console.log('\n\nCALL getFinishReservationListData');
    /*
        {"user": { "id": "test" }}
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: { wait: [] } };
        try {
            let query = `SELECT OWNER_NAME, PET_NAME, REGIST_STATE
            from RESERVATION_TB where REGIST_STATE = '4';`
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
            let query = `SELECT OWNER_NAME, PET_NAME, REGIST_STATE
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
                console.log(rows)
                ret.result = rows[0]
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

app.post('/getReservationCount', async(req, res, next) => {
    console.log('\n\nCALL getReservationCount');
    /*
        {
            "HOSPITAL_KEY" : 2350
        }
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body;
        try {
            let query = `SELECT RESERVATION_COUNT FROM HospitalInfo_TB where HOSPITAL_KEY = ${user.HOSPITAL_KEY}`
            await connection.query(query, function(err, rows, fields) {
                console.log(rows)
                const ret = rows[0]
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
                query = `SELECT * from HospitalInfo_TB`
            else
                query = `SELECT HOSPITAL_KEY, CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2, SIGNUP_APP, RESERVATION_COUNT 
                from HospitalInfo_TB WHERE CEO_NAME LIKE '%${searchword}%' OR HOSPITAL_NAME LIKE '%${searchword}%';`

            // console.log(query)
            await connection.query(query, function(err, rows, fields) {
                console.log("ROWs : " + rows)
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
            "HOSPITAL_KEY" : "int"
            "TYPE" : "int"
            "OWNER_NAME" : "string"
            "OWNER_RESIDENT" : "string"
            "OWNER_PHONE_NUMBER" : "string"
            "OWNER_ADDRESS1" : "string"
            "OWNER_ADDRESS2" : "string"
            "PET_NAME" : "string"
            "PET_VARIETY" : "string"
            "PET_COLOR" : "string"
            "PET_GENDER" : "int"
            "PET_NEUTRALIZATION" : "int"
            "PET_BIRTH" : "string"
            "ASK_DATE" : "string"
            "ETC" : "string"
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
        console.log(msg)
        const ret = { result: 0 };
        try {
            let query = `INSERT INTO RESERVATION_TB 
                (ID, 
                HOSPITAL_KEY, TYPE, OWNER_NAME, OWNER_RESIDENT, OWNER_PHONE_NUMBER, OWNER_ADDRESS1, OWNER_ADDRESS2, 
                PET_NAME, PET_VARIETY, PET_COLOR, PET_GENDER, PET_NEUTRALIZATION, PET_BIRTH, ASK_DATE, ETC, REGIST_STATE) 
            VALUES 
                ( (SELECT ID from USER_TB where HOSPITAL_KEY = '${msg.HOSPITAL_KEY}'), 
                '${msg.HOSPITAL_KEY}', '${msg.TYPE}', '${msg.OWNER_NAME}', '${msg.OWNER_RESIDENT}', '${msg.OWNER_PHONE_NUMBER}', '${msg.OWNER_ADDRESS1}', '${msg.OWNER_ADDRESS2}', 
                '${msg.PET_NAME}', '${msg.PET_VARIETY}', '${msg.PET_COLOR}', '${msg.PET_GENDER}', '${msg.PET_NEUTRALIZATION}', '${msg.PET_BIRTH}', '${msg.ASK_DATE}', '${msg.ETC}', '1');`

            await connection.query(query, function(err, result) {
                console.log("connection query")
                console.log(query)
                try {
                    console.log(err, result)
                    if (result.affectedRows != 0) { // 잘 들어갔다
                        console.log("1 : " + result)
                        ret.result = 1; // 성공
                        query = `UPDATE HospitalInfo_TB SET RESERVATION_COUNT = RESERVATION_COUNT + 1 where hospital_key = '${msg.HOSPITAL_KEY}';`
                        try {
                            connection.query(query, function(err, result) {
                                console.log("2 : " + result)
                                if (result.affectedRows != 0) { // 잘 들어갔다
                                    ret.result = 1;
                                } else {
                                    ret.result = 0; // 실패
                                }
                            })
                        } catch (err) {
                            ret.result = err;
                            connection.release(); // db 연결 끝
                            res.json(ret)
                        }
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
 * COMMUNITY
 */

app.post('/putCommunity', async(req, res, next) => {
    console.log('\n\nCALL putCommunity');
    /*
        {
            "user": {
                "article_index" : 1,
                "title": "집에 보내줘",
                "article_author": "김가연",
                "article_date": "2019-07-02",
                "article_content": "집에 가고싶어요..",
                "img_url_1": "",
                "img_url_2": "",
                "img_url_3": ""
            }
        }
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        const imgs = ["", "", "", ""];
        try {
            Array.prototype.forEach.call(imgs, (el, idx) => {
                if (idx == 0 || !user[`img_url_${idx}`]) {} else {
                    const file_path = `${article_file_path}${user.article_index}_${idx}.bmp`
                    const bitmap = Buffer.from(user[`img_url_${idx}`], 'base64');
                    fs.writeFileSync(file_path, bitmap);
                    imgs[idx] = file_path;
                    console.log(`${file_path} : ******** base64로 인코딩되었던 파일 쓰기 성공 ********`);
                }
            })

            let query = `INSERT INTO COMMUNITY_ARTICLE_TB ( ARTICLE_INDEX, TITLE, ARTICLE_AUTHOR, ARTICLE_DATE, ARTICLE_CONTENT, IMG_URL_1, IMG_URL_2, IMG_URL_3) 
            VALUES('${user.article_index}', '${user.title}', '${user.article_author}', '${user.article_date}', '${user.article_content}', '${imgs[1]}', '${imgs[2]}', '${imgs[3]}');`

            await connection.query(query, function(err, rows, fields) {
                console.log(err)
                console.log(rows)
                if (rows) {
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

app.post('/putCommentDB', async(req, res, next) => {
    console.log('\n\nCALL putCommentDB');
    /*
        {
            "user": {
                "article_index" : 1,
                "comment_author": "미리내병원",
                "comment_date": "2019.07.03",
                "comment_content": "커뮤니티가 끝나간당"
            }
        }
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: 0 };
        try {
            let query = `INSERT INTO COMMUNITY_COMMENT_TB ( ARTICLE_INDEX, COMMENT_AUTHOR, COMMENT_DATE, COMMENT_CONTENT) 
            VALUES('${user.article_index}', '${user.comment_author}', '${user.comment_date}', '${user.comment_content}');`

            await connection.query(query, function(err, rows, fields) {
                if (rows) {
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

app.post('/getCommentListData', async(req, res, next) => {
    console.log('\n\nCALL getCommentListData');
    /*
        {"user":{"article_index":2}} 

        {
            "result": {
                "content": [
                    {
                        "ARTICLE_DATE": "2019-06-24",
                        "ARTICLE_CONTENT": "랄랄라라ㅏ",
                        "IMG_URL_1": "ASFSES",
                        "IMG_URL_2": "ASFSES",
                        "IMG_URL_3": "NULL"
                    }
                ],
                "comment": [
                    {
                        "COMMENT_AUTHOR": "김가연",
                        "COMMENT_DATE": "2019-06-24",
                        "COMMENT_CONTENT": "그게 글이냐!!"
                    },
                    {
                        "COMMENT_AUTHOR": "정지원",
                        "COMMENT_DATE": "2019-07-24",
                        "COMMENT_CONTENT": "그래 글이다!!"
                    }
                ]
            }
        }       
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: { content: [], comment: [] } };
        try {
            let query = `SELECT ARTICLE_DATE, ARTICLE_CONTENT, IMG_URL_1, IMG_URL_2, IMG_URL_3 FROM COMMUNITY_ARTICLE_TB where ARTICLE_INDEX = '${user.article_index}';`

            await connection.query(query, async function(err, rows, fields) {
                ret.result.content = rows

                // for (let idx of range(1, 3)) {
                for (let idx = 1; idx < 4; idx++) {
                    if (rows[0][`IMG_URL_${idx}`]) {
                        console.log(idx)
                        const file_path = rows[0][`IMG_URL_${idx}`];
                        console.log(file_path)
                        const bitmap = fs.readFileSync(file_path);
                        const tmp = Buffer.from(bitmap).toString('base64');
                        ret.result.content[0][`IMG_URL_${idx}`] = tmp;
                    }
                }

                try {
                    query = `SELECT COMMENT_AUTHOR, COMMENT_DATE, COMMENT_CONTENT FROM COMMUNITY_COMMENT_TB where ARTICLE_INDEX = '${user.article_index}';`
                    await connection.query(query, function(err, rows, fields) {
                        if (rows) {
                            ret.result.comment = rows
                        }
                        connection.release();
                        res.json(ret)
                    });
                } catch (err) {
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

app.post('/getCommunityListData', async(req, res, next) => {
    console.log('\n\nCALL getCommunityListData');
    /*
        {"user": 1}

        {
            "result": {
                "community": [
                    {
                        "TITLE": "뿡이는 뭐할까",
                        "ARTICLE_AUTHOR": "김가연",
                        "ARTICLE_INDEX": 1
                    },
                    {
                        "TITLE": "지원아 일어나",
                        "ARTICLE_AUTHOR": "정지원",
                        "ARTICLE_INDEX": 2
                    }
                ]
            }
        }        
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: { community: [] } };
        try {
            let query = `SELECT TITLE, ARTICLE_AUTHOR, ARTICLE_INDEX FROM COMMUNITY_ARTICLE_TB;`

            await connection.query(query, function(err, rows, fields) {
                ret.result.community = rows
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

app.post('/getSearchCommunity', async(req, res, next) => {
    console.log('\n\nCALL getSearchCommunity');
    /*
        {"user": { 
            "select": 1 / 2,    // 1 : 작성자, 2 : 제목
            "key": "뿡이"
        }}

        {
            "result": {
                "community": [
                    {
                        "TITLE": "뿡이는 뭐할까",
                        "ARTICLE_AUTHOR": "김가연",
                        "ARTICLE_INDEX": 1
                    },
                    {
                        "TITLE": "지원아 일어나",
                        "ARTICLE_AUTHOR": "정지원",
                        "ARTICLE_INDEX": 2
                    }
                ]
            }
        }        
    */
    try {
        const connection = await pool.getConnection(async conn => conn);
        const user = req.body.user;
        const ret = { result: { community: [] } };
        try {
            let query;
            if (user.select == '2') {
                query = `SELECT TITLE, ARTICLE_AUTHOR, ARTICLE_INDEX FROM COMMUNITY_ARTICLE_TB 
                    where TITLE LIKE "%${user.key}%";`
            } else if (user.select == '1') {
                query = `SELECT TITLE, ARTICLE_AUTHOR, ARTICLE_INDEX FROM COMMUNITY_ARTICLE_TB 
                    where ARTICLE_AUTHOR LIKE "%${user.key}%";`
            }

            await connection.query(query, function(err, rows, fields) {
                ret.result.community = rows
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
            query = `INSERT INTO HospitalInfo_TB (HOSPITAL_KEY, CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2) 
            VALUES `

            await connection.query('SET FOREIGN_KEY_CHECKS = 0;') // foreign key 무시
            await connection.query('truncate HospitalInfo_TB;') // table 비운다
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
        console.log(err)
        console.log('DB Error');
        return false;
    }
}