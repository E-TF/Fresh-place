import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
import {formatPrice} from "./Cart";

function Items() {

    const [items, setItems] = useState([]);
    const {categoryName} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(`/public/items/category?codeEngName=${categoryName}`)
            .then(data => {
                const copy = [...data.data];
                setItems(copy);
            })
            .catch(error => {
                alert(error.response.data.error)
            })
    }, [categoryName])

    return (

        <>
            <div className="standard-area">

                {items != null ?
                    <div className="container">
                    <div className="row">
                        {
                            items.map((item) =>
                                <div key={item.itemSeq} className="col-md-4">
                                    <img alt={'thumbNail'} src={item.thumbNailImage} width="100%" height="100%" onClick={() => navigate(`/public/item/${item.itemSeq}`)}/>
                                    <h4 className="pt-5">{item.itemName}</h4>
                                    <p>{item.price && formatPrice(item.price)}원</p>
                                </div>
                            )
                        }
                    </div>
                </div> : null}

            </div>

        </>

    )
}

export default Items;
