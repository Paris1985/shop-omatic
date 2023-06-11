<html>
   <head></head>
   <body>
      <h1>Welcome to Sport Store</h1>
      <h3>Products</h3>
        <ul th:each="product : ${products}">
          <li th:text="${product}"></li>
        </ul>
   </body>
</html>