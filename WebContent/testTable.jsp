<!DOCTYPE html>
<html>
<meta charset="utf-8">
<head>
<title>Table With Embedded Line Chart</title>
<script src="http://d3js.org/d3.v3.min.js"></script>
<link rel="stylesheet" href="http://billmill.org/css/style.css" />

<script src="displayScripts/drawTableRuns.js"> </script>

<style>
table {
  border-collapse: collapse;
}
th {
  border-bottom: 2px solid #ddd;
  padding: 8px;
  font-weight: bold;
}
td {
  padding: 8px;
  border-top: 1px solid #ddd;
}
#chart {
  padding: 0px;
}
.xaxislabel {
  font-size: 9px;
}
</style>
</head>
<body>


<script>

function popupwindow(url, title, w, h) {
	  var left = (screen.width/2)-(w/2);
	  var top = (screen.height/2)-(h/2);
	  return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	} 

</script>



<button type="button" onclick="popupwindow('index.html', 'yo',500,600)">Click Me!</button>

<div id="datatableruns"></div>

<script type = 'text/javascript'>

var rows = [{date: "2013-08-10T00:00:00",dt: "1",id: "runTwo", pot: "29.2",step: "50"},{date: "2013-08-10T00:00:00",dt: "2",id: "runOne", pot: "273.02",step: "50"},
            ];
            
initial_display();
</script>

</body>
</html>