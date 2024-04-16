<?php
include('user.php');

$testUser = new User('test_user', 'Stg0nPassw0rd', false);

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $submittedUsername = $_POST['username'];
    $submittedPassword = $_POST['password'];

    if ($submittedUsername == $testUser->getUsername() && $submittedPassword == $testUser->getPassword()) {
setcookie('user', serialize($testUser), time() + 420, '/');
header("Location: dashboard.php");
exit();
    } else {
        $error = "Invalid username or password.";
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<!--  'test_user', 'Stg0nPassw0rd' -->
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form method="post">
        <label for="username">Username:</label><br>
        <input type="text" id="username" name="username"><br>
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password"><br><br>
        <input type="submit" value="Login">
    </form>
    <?php if (isset($error)) {
        echo "<p>$error</p>";
    } ?>
</body>
</html>
