<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <title></title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width">
  <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
  <!-- build:css(.) styles/vendor.css -->
  <!-- bower:css -->
  <link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap.css" />
  <!-- endbower -->
  <!-- endbuild -->
  <!-- build:css(.tmp) styles/main.css -->
  <link rel="stylesheet" href="/styles/main.css">
  <!-- endbuild -->
</head>
<body>
<!--[if lte IE 8]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->

<!-- Add your site or application content here -->
<div class="header">
  <div class="navbar navbar-default" role="navigation">


    <a class="navbar-brand" href="index.html">WallaLinks!</a>

    <div class="collapse navbar-collapse" id="js-navbar-collapse">
      <ul class="nav navbar-nav">
        <li ><a href="index.html">Home</a></li>
      </ul>
    </div>
  </div>
</div>

<div class="container" style="text-align:center;">
  <img src="images/lock.png" class="lockImage">
  <p class="lockText">403: This link is private </p>
  <p class="lockTextSecondary">Either no token or an incorrect one has been supplied. </p>

<div class="container">
  <div class="input-group">
    <input type="text" id="token" class="form-control" placeholder="Enter the private token here...">
      <span class="input-group-btn">
        <button id="goButton" class="btn btn-default" type="button">Go!</button>
      </span>
  </div><!-- /input-group -->
</div><!-- /.col-lg-6 -->



</div>
<script type="text/javascript">

  document.getElementById("goButton").onclick = function () {
    var privateToken = document.getElementById("token").value;
    location.href = "${hash}?privateToken=" + privateToken;
  };
</script>
</body>
</html>
