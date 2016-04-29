/*
 * x$ function to be able to use the jQuery selectors with XPage id's 
 * 
 *  by Mark Roden, http://openntf.org/XSnippets.nsf/snippet.xsp?id=x-jquery-selector-for-xpages
 */

function x$(idTag, param){ //Updated 18 Feb 2012
   idTag=idTag.replace(/:/gi, "\\:")+(param ? param : "");
   return($("#"+idTag));
}

$(document).on('change', '.btn-file :file', function() {
	  var input = $(this),
	      numFiles = input.get(0).files ? input.get(0).files.length : 1,
	      label = input.val().replace(/\\/g, '/').replace(/.*\//, ''); 
	  //input.trigger('fileselect', [numFiles, label]);
	      var input = $(this).parents('.input-group').find(':text'),
          log = numFiles > 1 ? numFiles + ' files selected' : label;
      if( input.length ) {
          input.val(log);
      } else {
          if( log ) alert(log);
      }
	});

	$(document).ready( function() {
	    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
	    	console.log('trigger');
	        var input = $(this).parents('.input-group').find(':text'),
	            log = numFiles > 1 ? numFiles + ' files selected' : label;
	        if( input.length ) {
	            input.val(log);
	        } else {
	            if( log ) alert(log);
	        }
	        
	    });
	});

	$( document ).ready(function(){
		$(".table a[role='button']").addClass( "btn btn-danger" );
		$(".table a[role='button']").html( "<icon class='fa fa-recycle'></icon>" );
	})
	
