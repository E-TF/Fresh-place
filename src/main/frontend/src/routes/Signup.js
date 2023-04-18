import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import {useForm} from "react-hook-form";
import axios from "axios";

function Signup(props) {

    const {
        register,
        handleSubmit,
        formState : {errors}
    } = useForm();

    const onSubmit = async (formData) => {
        await axios.post('/api/public/signup', formData, {headers: {"Content-Type": `application/json`}})
            .then(() => {
                alert('축하합니다! 회원가입에 성공하였습니다.');
                props.navigate('/login');
            })
            .catch((error) => alert(error));
    }

    return(

        <>
            <div className="standard-area">

                <Form style={{backgroundColor:''}} onSubmit={handleSubmit(onSubmit)}>

                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="memberId" >
                            <Form.Label>* 아이디</Form.Label>
                            <Form.Control {...register("memberId", {
                                required: "아이디는 필수 입력 값 입니다.",
                                minLength: {
                                    value:7,
                                    message:"아이디는 7글자 이상 12글자 이하 입니다."
                                },
                                maxLength: {
                                    value:12,
                                    message: "아이디는 7글자 이상 12글자 이하 입니다."
                                },
                            })}/>
                            {errors.memberId && <p>{errors.memberId.message}</p>}
                        </Form.Group>

                        <Form.Group as={Col} controlId="memberName">
                            <Form.Label>* 이름</Form.Label>
                            <Form.Control {...register("memberName", {
                                required: "이름은 필수 입력 값 입니다.",
                                minLength: {
                                    value:2,
                                    message:"이름은 최소 2글자 입니다."
                                },
                            })}/>
                            {errors.memberName && <p>{errors.memberName.message}</p>}
                        </Form.Group>

                        <Form.Group as={Col} controlId="password">
                            <Form.Label>* 비밀번호</Form.Label>
                            <Form.Control type="password" {...register("password", {
                                required: "비밀번호는 필수 입력 값 입니다.",
                                pattern: {
                                    value: /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,16}$/,
                                    message: '비밀번호를 8~16자로 영문 대소문자, 숫자, 특수기호를 조합해서 사용하세요. '
                                }
                            })} />
                            {errors.password && <p>{errors.password.message}</p>}
                        </Form.Group>
                    </Row>

                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="phoneNumber">
                            <Form.Label>* 핸드폰</Form.Label>
                            <Form.Control {...register("phoneNumber", {
                                required: "핸드폰은 필수 입력 값 입니다.",
                                pattern: {
                                    value:"^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
                                    message:"01*-****-**** 형식을 지켜주세요."
                                }
                            })} />
                            {errors.phoneNumber && <p>{errors.phoneNumber.message}</p>}
                        </Form.Group>

                        <Form.Group as={Col} controlId="email">
                            <Form.Label>* 이메일</Form.Label>
                            <Form.Control type="email" {...register("email", {
                                required: "이메일은 필수 입력 값 입니다.",
                                pattern: {
                                    value:
                                        /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i,
                                    message: "이메일 형식에 맞지 않습니다.",
                                },
                            })}/>
                            {errors.email && <p>{errors.email.message}</p>}
                        </Form.Group>

                        <Form.Group as={Col} controlId="memberBirth">
                            <Form.Label>* 생년월일</Form.Label>
                            <Form.Control type="date" {...register("memberBirth", {
                                required: "생일은 필수 입력 값 입니다."
                            })} />
                            {errors.memberBirth && <p>{errors.memberBirth.message}</p>}
                        </Form.Group>
                    </Row>

                    <Button variant="primary" type="submit">
                        가입하기
                    </Button>

                </Form>

            </div>

        </>

    )

}

export default Signup;
