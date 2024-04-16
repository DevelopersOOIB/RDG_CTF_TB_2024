<?php
session_start();

if (!isset($_COOKIE['user'])) {
    header("Location: index.php");
    exit();
}

include('user.php');

$currentUser = unserialize($_COOKIE['user'], ["allowed_classes" => ["User"]]);

if ($currentUser->isAdmin()) {
    header("Location: pinging_only_for_true_admin.php");
    exit();
} else {
    echo "<h1>Welcome, " . $currentUser->getUsername() . "!</h1>";
    echo "<p>You are not an admin.</p>";
    echo '<img src="notadmin.jpg" alt="Non-Admin Special Image">';
    echo '<br><a href="logout.php">Logout</a>';
}
?>
