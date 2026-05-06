import { useState } from 'react';
import { useAuth } from '../contexts/useAuth';
import { toast } from 'react-toastify';

export default function Login() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { loginUser } = useAuth();

    const login = async () => {

        loginUser(email, password);
        toast.success('Login realizado com sucesso!');
    }

    return (

        <div>
            <div className='mt-5 primary-container'>

                <div className='primary-box'>

                    <h3>Login</h3>

                    <div className="mb-3 mt-4">

                        <label htmlFor="exampleInputEmail1" className="form-label">Email</label>
                        <input type="email" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp"
                            onChange={(e) => setEmail(e.target.value)} />

                    </div>

                    <div className="mb-3">

                        <label htmlFor="inputPassword" className="col-sm-2 col-form-label">Senha</label>
                        <input type="password" className="form-control" id="inputPassword"
                            onChange={(e) => setPassword(e.target.value)} />

                    </div>

                    <div className="d-grid gap-2">
                        
                        <button type="button" className="btn btn-success mb-2" onClick={login}>Logar</button>

                    </div>

                </div>

            </div>
        </div>
    );
}