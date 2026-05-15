import { useEffect, useState } from "react";
import { useAuth } from "../contexts/useAuth";
import { toast } from 'react-toastify';
import api from "../services/api";

export default function Register() {

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmationPassword, setConfirmationPassword] = useState('');
    const [disableRegistration, setDisableRegistration] = useState(true);

    const { registerUser } = useAuth();

    useEffect(() => {

        const verifyFields = () => {
            
            setDisableRegistration(true);

            if(username && email && password && confirmationPassword) {

                setDisableRegistration(false);
            }
        }
    
        verifyFields();
    }, [username, email, password, confirmationPassword]);

    const register = async () => {

        if(equalPasswords()) {

            const resUsername = await api.get(`/users?username=${username}`);

            if(resUsername.status === 200) {

                toast.error('Esse nome de usuário não está disponível!');
                return;
            }

            const resEmail = await api.get(`/users?email=${email}`);

            if(resEmail.status === 200) {

                toast.error('Email já cadastrado!');
                return;
            }

            registerUser(email, username, password);

            toast.success('Registro realizado com sucesso!');
        }else {

            toast.error('As senhas devem ser iguais!');
        }
    }

    const equalPasswords = () => {

        return password === confirmationPassword;
    }

    return (

        <div>

            <div className='mt-5 primary-container'>

                <div className='primary-box'>

                    <h3>Registre-se</h3>

                    <div className="mb-3 mt-4">

                        <label htmlFor="InputUsername" className="form-label">Nome de usuário</label>
                        <input type="text" className="form-control" id="InputUsername" aria-describedby="usernameHelp"
                            onChange={(e) => setUsername(e.target.value)} required/>
                    </div>

                    <div className="mb-3 mt-4">

                        <label htmlFor="InputEmail" className="form-label">Email</label>
                        <input type="email" className="form-control" id="InputEmail" aria-describedby="emailHelp"
                            onChange={(e) => setEmail(e.target.value)} required/>

                    </div>

                    <div className="mb-3">

                        <label htmlFor="inputPassword" className="col-sm-2 col-form-label">Senha</label>
                        <input type="password" className="form-control" id="inputPassword"
                            onChange={(e) => setPassword(e.target.value)} required/>

                    </div>

                    <div className="mb-3">

                        <label htmlFor="inputConfirmationPassword" className="col-sm-3 col-form-label">Confirme a senha</label>
                        <input type="password" className="form-control" id="inputConfirmationPassword"
                            onChange={(e) => setConfirmationPassword(e.target.value)} required/>

                    </div>

                    <div className="d-grid gap-2">
                        
                        <button type="submit" className="btn btn-success mb-2" onClick={register} disabled={disableRegistration}>Registrar</button>

                    </div>

                </div>

            </div>

        </div>
    );
}