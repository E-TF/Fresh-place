import './App.css'
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap"
import {Routes, Route, useNavigate} from "react-router-dom"
import {useEffect, useState} from "react";
import axios from 'axios'
import Items from "./routes/Items";
import Signup from "./routes/Signup";
import {getAuthorization, Login, Logout} from "./routes/Authentication";
import Item from "./routes/Detail";
import Cart from "./routes/Cart";

function App() {

    const navigate = useNavigate();
    const [category, setCategory] = useState([]);
    const [loginStatus, setLoginStatus] = useState();

    useEffect(() => {
        axios.get('/api/public/category')
            .then(data => {
                setCategory(data.data);
            })
            .catch(() => {
                const copy = [...'...']
                setCategory(copy)
            })
    }, []);

    useEffect(() => {
        getAuthorization() ? setLoginStatus(getAuthorization()) : setLoginStatus(null);
    }, []);

    return (
        <div className="App">

            <Navbar bg="light" expand="lg">
                <Container>
                    <Navbar.Brand href="/">FreshPlace</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">

                            <NavDropdown title="Category" id="basic-nav-dropdown">
                                {
                                    category.map((category) =>
                                        <>
                                            <NavDropdown title={category.mainCategoryKorName} id="basic-nav-dropdown"
                                                         key={category.mainCategoryEngName}>
                                                {
                                                    category.subCategoryResponses.map((subCategory) =>
                                                        <>
                                                            <NavDropdown.Item key={subCategory.subCategoryEngName}
                                                                              onClick={() => {
                                                                                  navigate(`public/items/category/${subCategory.subCategoryEngName}`)
                                                                              }}
                                                                              disabled={
                                                                                  subCategory.subCategoryEngName !== 'driedFruit'
                                                                                  && subCategory.subCategoryEngName !== 'coffeeDrink'
                                                                              }>
                                                                        {subCategory.subCategoryKorName}

                                                            </NavDropdown.Item>
                                                            <NavDropdown.Divider/>
                                                        </>
                                                    )
                                                }
                                            </NavDropdown>
                                        </>
                                    )
                                }
                            </NavDropdown>

                            <Nav.Link href="/public/cart">Cart</Nav.Link>
                            {loginStatus == null ?
                                <>
                                    <Nav.Link href="/login">Login</Nav.Link>
                                    <Nav.Link href="/public/signup">Signup</Nav.Link>
                                </>
                                :
                                <>
                                    <Nav.Link href={"/members/logout"}>Logout</Nav.Link>
                                </>
                            }
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            <Routes>
                <Route path="/" element={
                    <>
                        <div className="main-bg"/>
                    </>
                }/>
                <Route path="/public/items/category/:categoryName" element={<Items/>}/>
                <Route path="/public/signup" element={<Signup/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/public/item/:itemSeq" element={<Item/>}/>
                <Route path="/public/cart" element={<Cart/>}/>
                <Route path="/members/logout" element={<Logout/>}/>
            </Routes>
        </div>

    );
}

export default App;
