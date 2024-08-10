document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('error-message');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('http://localhost:8080/apis/access/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    username: username,
                    password: password
                })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('authToken', data.token);
                window.location.href = 'index.html';
            } else {
                errorMessage.classList.remove('hidden');
                errorMessage.textContent = 'Usuário ou senha inválidos!';
            }
        } catch (error) {
            console.error('Erro ao tentar autenticar:', error);
            errorMessage.classList.remove('hidden');
            errorMessage.textContent = 'Ocorreu um erro ao tentar autenticar!';
        }
    });
});
