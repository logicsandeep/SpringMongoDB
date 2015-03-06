<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>


<style type="text/css">
/* tables */
table.tablesorter {
    font-family:arial;
    background-color: #CDCDCD;
    margin:10px 0pt 15px;
    font-size: 8pt;
    width: 100%;
    text-align: left;
}
table.tablesorter thead tr th, table.tablesorter tfoot tr th {
    background-color: #e6EEEE;
    border: 1px solid #FFF;
    font-size: 8pt;
    padding: 4px;
}
table.tablesorter thead tr .header {
    background-image: url(bg.gif);
    background-repeat: no-repeat;
    background-position: center right;
    cursor: pointer;
}
table.tablesorter tbody td {
    color: #3D3D3D;
    padding: 4px;
    background-color: #FFF;
    vertical-align: top;
}
table.tablesorter tbody tr.odd td {
    background-color:#F0F0F6;
}
table.tablesorter thead tr .headerSortUp {
    background-image: url(asc.gif);
}
table.tablesorter thead tr .headerSortDown {
    background-image: url(desc.gif);
}
table.tablesorter thead tr .headerSortDown, table.tablesorter thead tr .headerSortUp {
background-color: #8dbdd8;
}
</style>
<script src="http://www.datatables.net/release-datatables/media/js/jquery.js" type="text/javascript"></script> 
<script src="http://www.datatables.net/release-datatables/media/js/jquery.dataTables.js" type="text/javascript"></script>


<script type="text/javascript">
//    $(document).ready(function() {      
//        $("#table-2").tablesorter();
//        $(".f_txt").table_filter({'table':'.tablesorter'});
 //   });
 
 $(document).ready(function() {
     var asInitVals = new Array();
    var oTable = $('#table-2').dataTable( {
        "oLanguage": {
            "sSearch": "Search all columns:"
        }
    } );
    
    $("thead input").keyup( function () {
        /* Filter on the column (the index) of this element */
        //alert('index'+ $("thead input").index(this));
        oTable.fnFilter( this.value, $("thead input").index(this));
    } );
    
    
    
    /*
     * Support functions to provide a little bit of 'user friendlyness' to the textboxes in 
     * the footer
     */
    $("thead input").each( function (i) {
        asInitVals[i] = this.value;
    } );
    
    $("thead input").focus( function () {
        if ( this.className == "search_init" )
        {
            this.className = "";
            this.value = "";
        }
    } );
    
    $("thead input").blur( function (i) {
        if ( this.value == "" )
        {
            this.className = "search_init";
            this.value = asInitVals[$("thead input").index(this)];
        }
    } );
} );
    </script>
</head>
<body>
	<h2>Duplicate PrismGuid Determiner.</h2>

	<!-- >	<form action="person/save" method="post">
			<input type="hidden" name="id">
			<label for="name">Person Name</label>
			<input type="text" id="name" name="name"/>
			<input type="submit" value="Submit"/>
		</form> -->
		
		<input type="text" class="f_txt"/>
	<h2> Total Number of Duplicates:<c:out value="${fn:length(personList)}"></c:out></h2>
	<table id="table-2" class="tablesorter">
	
	<thead>
		  <tr>
 	       <th><input type="text" name="search_sno" value="Search SNO" class="search_init" /></th>
            <th><input type="text" name="search_engine" value="Search PrismGuid" class="search_init" /></th>
            <th><input type="text" name="search_browser" value="Search Buckets" class="search_init" /></th>
            <th><input type="text" name="search_platform" value="Search size" class="search_init" /></th>
            <th><input type="text" name="search_version" value="Search Correct Bucket#" class="search_init" /></th>
        </tr>
	</thead>
		<c:forEach var="person" items="${personList}" varStatus="loopStatus">
			<tr>
			<td><c:out value="${loopStatus.index+1}"></c:out></td>
				<td>${person.prismGuid}</td>
				<td>
				<c:forEach var="bucket" items="${person.buckets}">
					${bucket}|
				</c:forEach>
				</td>
				<td>${person.count}</td>
				<td>CF${person.correctBucket}</td>
				<!-- <td><input type="button" value="delete" onclick="window.location='person/delete?id=${person.prismGuid}'"/></td> -->
			</tr>
		</c:forEach>
	</table>	
</body>
</html>