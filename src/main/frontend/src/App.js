import './App.css';
import React, {useEffect, useState} from 'react';
import axios from 'axios';

function App() {

    const [hello, setHello] = useState('')

    useEffect(() => {
        axios.get('/public/main')
            .then(response => setHello(response.data))
            .catch(error => console.log(error))
    }, []);

  return(
      <div className="App">

          데이터  : {hello}

      </div>

  )
}


export default App;
