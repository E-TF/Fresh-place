import './App.css'
import {Container, Nav, Navbar, NavDropdown} from "react-bootstrap"
import {Routes, Route, useNavigate, Outlet} from "react-router-dom"
import {useEffect, useState} from "react";
import axios from 'axios'
import Items from "./routes/Items";
import Detail from "./routes/Detail";

function App() {

    let [category, setCategory] = useState([]);
    let navigate = useNavigate();

    useEffect(() => {
        function getItems() {
            axios.get('/public/category')
                .then(data => {
                    setCategory(data.data);
                })
        }
        getItems()
    }, [])

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
                                            <NavDropdown title={category.korName} id="basic-nav-dropdown"
                                                         key={category.engName}>
                                                {
                                                    category.subCategoryResponses.map((subCategory) =>
                                                        <>
                                                            <NavDropdown.Item key={subCategory.engName} onClick={() => {
                                                                navigate('public/items/category/' + subCategory.engName)
                                                            }}>
                                                                {subCategory.korName}
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

                            <Nav.Link href="/">Cart</Nav.Link>
                            <Nav.Link href="/">Login</Nav.Link>
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
                <Route path="/public/items/category/:categoryName" element={<Items navigate={navigate} />} />
                <Route path="/public/item/:itemSeq" element={<Detail />} />
            </Routes>

        </div>
    );
}

export default App;
