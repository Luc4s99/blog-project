export default function Footer() {

    const currentYear = new Date().getFullYear();

    return (
        <>
            <footer className="footer">

                <div>
                    © {currentYear} Copyright: 
                    <a className="text-reset fw-bold" href="https://github.com/Luc4s99">Lucas Mateus</a>
                </div>

            </footer>
        </>
    );
}