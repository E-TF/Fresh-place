import {useParams} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";

function Items(props) {

    let [items, setItems] = useState([]);
    let {categoryName} = useParams();

    useEffect(() => {
        axios.get(`/public/items/category?codeEngName=${categoryName}`)
            .then(data => {
                let copy = [...data.data];
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
                                    <img src={item.thumbNailImage} width="100%" height="100%" onClick={() => props.navigate(`/public/item/${item.itemSeq}`)}/>
                                    <h4 className="pt-5">{item.itemName}</h4>
                                    <p>{item.price}원</p>
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
