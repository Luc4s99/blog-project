import Footer from "../components/footer/Footer";
import Navbar from "../components/navbar/Navbar";

import landingImage from '../assets/thumbs-up.png'

export default function LandingPage() {

    return(

        <div className="blogBody">

            <Navbar />

            <div className="container">

                <h1 className="mt-5">Descubra novas histórias, aprenda sobre o que quiser!</h1>

                <h4>Entre ou cadastre-se.</h4>

                <img src={landingImage} />

            </div>

            <Footer />

        </div>
    );
}