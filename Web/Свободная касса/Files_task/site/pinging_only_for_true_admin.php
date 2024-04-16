<?php
session_start();

if (!isset($_COOKIE['user'])) {
    header("Location: index.php");
    exit();
}


if (isset($_GET['address'])) {
    $address = $_GET['address'];

    $pingResult = shell_exec("ping -c 4 $address");

    echo "<h1>Ping Results for $address</h1>";
    echo "<pre>$pingResult</pre>";

    echo '<br><a href="logout.php">Logout</a>';
} else {
    echo "<h1>Ping Tool</h1>";
    echo "<form method='get'>";
    echo "Address: <input type='text' name='address' required>";
    echo "<input type='submit' value='Ping'>";
    echo "</form>";

    echo '<br><a href="logout.php">Logout</a>';
}
?>
