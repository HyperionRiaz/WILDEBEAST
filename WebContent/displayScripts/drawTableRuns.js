
function update(runID)
{
alert("Hello World!" + runID);
}

function initial_display  () {


var formatdate = d3.time.format("%b %d %Y");




  console.log(rows)
  var table = d3.select("#datatableruns").append("table");
      thead = table.append("thead");
      tbody = table.append("tbody");

  thead.append("th").text("id");
  thead.append("th").text("Date");
  thead.append("th").text("Percent");
  thead.append("th").text("Potential");
  thead.append("th").text("Update");
  thead.append("th").text("Relative potential");
  
  var tr = tbody.selectAll("tr")
      .data(rows)
      .enter().append("tr");
  

  var td = tr.selectAll("td")
        .data(function(d) { return [ d.id, d.date, d.step, d.pot]; })
      .enter().append("td")
      //.append("xhtml:body")
   // .html(function(d,i) {
    //     return "<input type='button' value='lol'>"+ d+"</input>";
     //  });
        .text(function(d) { return d; });

//Now add the button column
  d3.selectAll("#datatableruns tbody tr").append("td")
  .append("xhtml:body")
  
  //NB PREVIOUS CODE FOR UPDATING
  //.html(function(d,i) { return "<a href='queryRun?runID="+d.id+"'><input type='button' style='background-color:lightgreen' value='Update'></input></a>"; });
  
  
  .html(function(d,i) { return "<button type='button' onclick=\"popupwindow('queryRun?runID="+d.id+"', 'yo',800,500)\">View information</button>"; });

  
  
  //NB IF YOU WANT TO CALL A FUNCTION
  //.html(function(d,i) { return "<input type='button' id='"+d.id+"' onclick='update(this.id)' style='background-color:lightgreen' value='Update'></input>"; });
       
   // .html("<input type='button' style='background-color:lightgreen' value='Update'></input>");
  //.attr("width", width/rows.length + "px")
  //.attr("rowspan", rows.length);
  
  var width = 80,
  height = d3.select("table")[0][0].clientHeight,
  mx = 4,
  radius = 2;
  
  //background-color:lightgreen;

  // Now add the chart column
  d3.select("#datatableruns tbody tr").append("td")
    .attr("id", "chart")
    .attr("width", width + "px")
    .attr("rowspan", rows.length);

  var chart = d3.select("#chart").append("svg")
      .attr("class", "chart")
      .attr("width", width)
      .attr("height", height);

  var maxpot = 0;
  var minpot = Number.MAX_VALUE;
  for (i=0; i < rows.length; i++) {
    if (rows[i].pot > maxpot) { maxpot = rows[i].pot; }
    if (rows[i].pot < minpot) { minpot = rows[i].pot; }
  }

  var dates = rows.map(function(t) { return t.dt; });

  var xscale = d3.scale.linear()
    .domain([0, 300])
    .range([mx, width-mx])
   .nice();

  var yscale = d3.scale.ordinal()
    .domain(dates)
    .rangeBands([0, height]);

  chart.selectAll(".xaxislabel")
      .data(xscale.ticks(2))
    .enter().append("text")
      .attr("class", "xaxislabel")
      .attr("x", function(d) { return xscale(d); })
      .attr("y", 10)
      .attr("text-anchor", "middle")
      .text(String)

  chart.selectAll(".xaxistick")
      .data(xscale.ticks(2))
    .enter().append("line")
      .attr("x1", function(d) { return xscale(d); })
      .attr("x2", function(d) { return xscale(d); })
      .attr("y1", 10)
      .attr("y2", height)
      .attr("stroke", "#eee")
      .attr("stroke-width", 1);

  chart.selectAll(".line")
      .data(rows)
    .enter().append("line")
      .attr("x1", function(d) { return xscale(d.pot); })
      .attr("y1", function(d) { return yscale(d.dt) + yscale.rangeBand()/2; })
      .attr("x2", function(d,i) { return rows[i+1] ? xscale(rows[i+1].pot) : xscale(d.pot); })
      .attr("y2", function(d,i) { return rows[i+1] ? yscale(rows[i+1].dt) + yscale.rangeBand()/2 : yscale(d.dt) + yscale.rangeBand()/2; })
      .attr("stroke", "#777")
      .attr("stroke-width", 1);

  var pt = chart.selectAll(".pt")
      .data(rows)
    .enter().append("g")
      .attr("class", "pt")
      .attr("transform", function(d) { return "translate(" + xscale(d.pot) + "," + (yscale(d.dt) + yscale.rangeBand()/2) + ")"; });

  pt.append("circle")
      .attr("cx", 0)
      .attr("cy", 0)
      .attr("r", radius)
      .attr("opacity", .5)
      .attr("fill", "#ff0000");
  




}