$(document).ready(function() {
	// Met à jour l'inputbox de l'URL
	var protocol = window.location.protocol;
	var hostname = window.location.hostname;
	var port = window.location.port;
	var fullpath = window.location.pathname;
	var appliPath = fullpath.substr(0, fullpath.indexOf('/',1) + 1);
	
	$('#appliUrl').val(protocol + '//' + hostname + ':' + port + appliPath);
	
	
    // Listener sur la soumission du formulaire
    $('#form').submit(function(e) {
      
      e.preventDefault();
      
      $.ajax({
          beforeSend: function(req) {
          if ( typeof($('#emailsList').val()) == 'undefined' ||  $('#emailsList').val() == '') {
              qui = $('#email').val();
              //alert('cas email');
          } else {
              qui = $('#emailsList').val();
              //alert('cas emailsList');
          }         
//client-cert: subject=emailAddress%3dchristine.brun@capgemini.com
          req.setRequestHeader("client-cert","subject=emailAddress%3d" + qui);
//            alert("Mail soumis -- > " + qui);
//            alert($('#appliUrl').val() + $('#methodPath').val());
          },
          type:"GET",
          url: $('#appliUrl').val() + $('#methodPath').val(),
          contentType: "text/plain; charset=utf-8",
          dataType: 'text',
          success: function(data){
//              alert(data);
              if (data.length == 0) {
//                alert('Erreur authentification');
              } else {
//            	  alert(data);
// VBT                 var appli = window.open("#","mywindow","height=750, location=no, menubar=no, toolbar=no ,width=1200, directories=no, resizable=yes, scrollbars=yes, status=yes");
                  try {
// VBT                	  appli.document.write(data);
						document.close();
						document.open();
						document.write(data);
						document.close();
                  } catch (e) {

                	  alert('Erreur chargement page intermédiaire');
                  }
              }
          }
       });
  });
});
