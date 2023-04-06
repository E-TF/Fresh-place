import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import {useState} from "react";
import axios from "../Interceptor";
import {useNavigate} from "react-router-dom";

export const authorizationName = 'accessToken';

export const getAuthorization = () => {
    return localStorage.getItem(authorizationName);
}

export const setAuthorization = (accessToken) => {
    localStorage.setItem(authorizationName, accessToken);
}

export const removeAuthorization = () => {
    localStorage.removeItem(authorizationName);
}

export const Login = () => {

    const navigate = useNavigate();
    const [memberId, setMemberId] = useState();
    const [password, setPassword] = useState();
    const memberIdHandler = (e) => setMemberId(e.target.value)
    const passwordHandler = (e) => setPassword(e.target.value);

    const onSubmit = async (e) => {
        await e.preventDefault()
        const formData = {
            "memberId" : memberId,
            "password" : password
        }

        await axios.post(
            '/login'
            , formData
            ,{
                headers: {
                    'Content-Type': `application/json`
                }
            })
            .then(response => {
                setAuthorization(response.data.accessToken);
                navigate('/');
            })
            .catch(error => {
                error.response.data.status === 400 ? alert(error.response.data.message) : alert('로그인을 다시 시도해주세요.');
            })
    }

    return (

        <>
            <div className="standard-area" style={{paddingLeft:"20%", paddingRight:"20%"}}>
                <Container className="panel">

                    <div>
                        <h1>로그인</h1>
                    </div>

                    <br /><br />

                    <Form onSubmit={onSubmit}>
                        <Form.Group as={Row} className="mb-3" controlId="memberId">
                            <Col sm>
                                <Form.Control type="text" placeholder="ID" onChange={memberIdHandler}/>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="password">
                            <Col sm>
                                <Form.Control type="password" placeholder="PASSWORD" onChange={passwordHandler}/>
                            </Col>
                        </Form.Group>
                        <br/>

                        <div className="d-grid gap-1">
                            <Button variant="primary" type="submit">
                                로그인
                            </Button>
                            <Button variant="secondary" type="button" href="/public/signup">
                                회원가입
                            </Button>
                        </div>
                    </Form>
                </Container>
            </div>
        </>

    );
}

export const ReissueAuthorization = (navigate) => {
    axios
        .patch('/reissue')
        .then(response => {
            setAuthorization(response.data.accessToken);
        })
        .catch((error) => {
            if (error.response.data.status === 401) {
                removeAuthorization();
            }
            alert('로그인을 다시 해주세요');
            navigate('/login');
        })
}

export const Logout = (navigate) => {
    axios
        .delete('/members/logout')
        .finally(() => {
            removeAuthorization();
            navigate('/');
        });
}
