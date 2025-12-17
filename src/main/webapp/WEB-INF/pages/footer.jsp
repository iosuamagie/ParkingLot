<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <style>
        /* 1. Asigură că body-ul ocupă cel puțin 100% din înălțimea ecranului */
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* 2. Main va ocupa tot spațiul disponibil, împingând footer-ul jos */
        main {
            flex: 1;
        }

        /* 3. Stilizare footer pentru centrare */
        footer {
            text-align: center; /* Centrează textul pe orizontală */
            padding-top: 3rem;
            padding-bottom: 3rem;
        }
    </style>
</head>
<body>

<main>
    <div class="container marketing">
    </div>
</main>

<footer class="container">
    <p>&copy; 2025 Dragotă Iosua Flavius</p>
</footer>

</body>
</html>