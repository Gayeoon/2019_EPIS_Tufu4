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

/*
    - USER_TB
    
    ID: varchar(20)   -> PK
    PW: varchar(30) NOT NULL
    HOSPITAL_KEY: varchar(20) NOT NULL   -> FK (HospitalInfo_TB.HOSPITAL_KEY)
    HOSPITAL_NAME: char(50) NOT NULL

    
    - HospitalInfo_TB
    
    HOSPITAL_KEY: int   -> PK
    CEO_NAME: char(30) NOT NULL
    HOSPITAL_NAME: char(50) NOT NULL
    PHONE_NUMBER: char(15) DEFAULT NULL
    ADDRESS1: char(80) DEFAULT NULL
    ADDRESS2: char(50) DEFAULT NULL
    SIGNUP_APP: tinyint(1) NOT NULL DEFAULT 0


    - RESERVATION_TB

    INDEX: int auto_increment   -> PK
    // 병원 정보 //
    ID: char(20) NOT NULL
    HOSPITAL_KEY: int   -> FK (HospitalInfo_TB.HOSPITAL_KEY)
    
    // 예약 정보 //
    OWNER_NAME: char(30) NOT NULL
    ADDRESS: char(120)
    PHONE_NUMBER: char(15)
    PET_NAME: char(30) NOT NULL
    RACE: char(30) NOT NULL
    PET_COLOR: char(15)
    PET_BIRTH: char(20)
    NEUTRALIZATION: int NOT NULL
    PET_GENDER: int NOT NULL
    
    // 예약 확인 //
    CONFIRM: int DEFAULT 0
*/

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

// app.post('/getHospitalName', async(req, res, next) => {
//     console.log('\n\nCALL getHospitalName');
//     /*
//         {"user": {"id": "test"}}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         const user = req.body.user;
//         const ret = { result: 0 };
//         try {
//             let query = `SELECT HOSPITAL_NAME from USER_TB where ID = '${user.id}';`
//             await connection.query(query, function(err, rows, fields) {
//                 // only 병원이 가입했을 경우
//                 if (rows.length) ret.result = rows[0].HOSPITAL_NAME;
//                 connection.release(); // db 연결 끝
//                 res.json(ret)
//             });

//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             res.json(ret)
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// });

// app.post('/getReservationCount', async(req, res, next) => {
//     console.log('\n\nCALL getReservationCount');
//     /*
//         {"user": {"id": "test"}}
//         신규예약    :    1
//         등록대기-1    :    2
//         등록대기-2    :    3
//         등록완료    :    4
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         const user = req.body.user
//         const ret = { result: 0 };
//         try {
//             console.log(user.id);
//             let query = `SELECT COUNT(*) as count from RESERVATION_TB where ID = '${user.id}';`
//             await connection.query(query, function(err, rows, fields) {
//                 console.log(`count : ${rows[0].count}`)
//                 ret.result = rows[0].count;
//                 connection.release(); // db 연결 끝
//                 res.json(ret)
//             });

//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             res.json(ret)
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// });

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


/* 
 * database 
 * 
 * async, await 필수 -> Promise 는 오류뜸
 */

// var rett = {
//     result: 0
// };

// const getIdCheck = async(payload) => {
//     /*
//         {"user": {"id": "test"}}
//     */
//     var rett = { result: 0 }
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         try {
//             let query = `SELECT ID from USER_TB where ID = '${payload.user.id}';`

//             // function(err, rows, fields) {
//             //     if (rows.length) {
//             //         rett.result = 1;
//             //         console.log(`${payload.user.id}가 중복됩니다`)
//             //     } else {
//             //         rett.result = 2;
//             //         console.log(`${payload.user.id}가 중복되지 않습니다`)
//             //     }
//             //     console.log("222222\n")
//             //         // connection.close();
//             //     connection.release();
//             //     console.log("33333\n")
//             // })

//             await connection.query(query, function(err, rows, fields) {
//                 // console.log(rows)
//                 if (rows.length) {
//                     rett.result = 1;
//                     console.log(`${payload.user.id}가 중복됩니다`)
//                 } else {
//                     rett.result = 2;
//                     console.log(`${payload.user.id}가 중복되지 않습니다`)
//                 }
//                 console.log("222222\n")
//             });

//             connection.release(); // db 연결 끝
//             console.log("33333\n")
//             console.log(rett)
//             return rett


//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }

// const getJoin = async(payload) => {
//     /*
//        {"user":{
//            "hospital":"힐링힐스동물병원", 
//            "name":"박성민", 
//            "number":"031-708-0078", 
//            "id":"test", 
//            "pw":"1234"
//         }}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         const user = payload.user;
//         try {
//             let query = `
//                 INSERT INTO USER_TB (ID, PW, HOSPITAL_KEY, HOSPITAL_NAME) 
//                 SELECT '${user.id}', '${user.pw}', HOSPITAL_KEY, HOSPITAL_NAME
//                 from HOSPITALINFO_TB 
//                 where HOSPITAL_NAME = '${user.hospital}' AND PHONE_NUMBER = '${user.number}';`
//             await connection.query(query);
//             rett.result = 1; // 성공
//             connection.release(); // db 연결 끝
//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             rett.result = 0; // 실패
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }

// const getLogin = async(payload) => {
//     /*
//         {"user": {
//             "id": "test",
//             "pw": "0000"
//         }}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         try {
//             let query = `SELECT HOSPITAL_KEY from USER_TB where ID = '${payload.user.id}' AND PW = '${payload.user.pw}';`
//             console.log(payload)
//             console.log(payload.user.id, payload.user.pw)

//             await connection.query(query, function(err, rows, fields) {
//                 if (rows.length) {
//                     rett.result = 1;
//                 } else {
//                     rett.result = 0;
//                 }
//                 console.log("length : " + rows.length)
//             });
//             connection.release(); // db 연결 끝

//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }

// const getThreeCheck = async(payload) => {
//     /*
//         {"user":{
//             "hospital":"미리내동물병원",
//             "name":"김가연",
//             "number":"031-574-7580"
//         }}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         try {
//             let query = `SELECT HOSPITAL_KEY from HOSPITALINFO_TB where CEO_NAME = '${payload.user.name}' AND PHONE_NUMBER = '${payload.user.number}';`
//             await connection.query(query, function(err, rows, fields) {
//                 if (rows.length) {
//                     rett.result = 1;
//                 } else {
//                     rett.result = 2;
//                 }
//                 console.log("length : " + rows.length)
//             });
//             connection.release(); // db 연결 끝

//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }

// const getHospitalName = async(payload) => {
//     /*
//         {"user": {"id": "test"}}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         try {
//             let query = `SELECT HOSPITAL_NAME from USER_TB where ID = '${payload.user.id}';`
//             await connection.query(query, function(err, rows, fields) {
//                 rett.result = rows[0].HOSPITAL_NAME;
//             });
//             connection.release(); // db 연결 끝

//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }

// const searchHospitalData = async(payload) => {
//     /*
//         {"searchword": "힐링동물병원"}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         try {
//             let query;

//             // 병원명이나 대표자명에 searchword가 포함된 col을 찾아서 보낸다.
//             if (payload.searchword == 'allHospitalData')
//                 query = `SELECT * from HOSPITALINFO_TB`
//             else
//                 query = `SELECT HOSPITAL_KEY, CEO_NAME, HOSPITAL_NAME, PHONE_NUMBER, ADDRESS1, ADDRESS2, SIGNUP_APP from HOSPITALINFO_TB
//             WHERE CEO_NAME LIKE '%${payload.searchword}%' OR HOSPITAL_NAME LIKE '%${payload.searchword}%';`

//             // console.log(query)
//             await connection.query(query, function(err, rows, fields) {
//                 // console.log("ROWs : " + rows)
//                 rett.result = rows;
//                 console.log(rett)
//             });
//             connection.release(); // db 연결 끝
//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }

// const getReservationCount = async(payload) => {
//     /*
//         {"user": {"id": "test"}}
//     */
//     try {
//         const connection = await pool.getConnection(async conn => conn);
//         try {
//             console.log(payload.user.id);
//             let query = `SELECT COUNT(*) as count from RESERVATION_TB where ID = '${payload.user.id}';`
//             await connection.query(query, function(err, rows, fields) {
//                 console.log(`count : ${rows[0].count}`)
//                 rett.result = rows[0].count;
//             });
//             connection.release(); // db 연결 끝

//         } catch (err) {
//             console.log('Query Error\n\n');
//             console.log(err);
//             connection.release();
//             return false;
//         }
//     } catch (err) {
//         console.log('DB Error');
//         return false;
//     }
// }