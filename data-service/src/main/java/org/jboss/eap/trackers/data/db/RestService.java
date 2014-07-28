/**
 * @author <a href="mailto:lgao@redhat.com">Lin Gao </a>
 */
package org.jboss.eap.trackers.data.db;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.eap.trackers.data.DataService;
import org.jboss.eap.trackers.data.DataServiceException;
import org.jboss.eap.trackers.model.Artifact;
import org.jboss.eap.trackers.model.Component;
import org.jboss.eap.trackers.model.Product;
import org.jboss.eap.trackers.model.ProductVersion;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * This is the REST Service place open to HTTP access.
 * 
 * It almost delegates all invocation to the DataService
 *  
 */
@Path("/")
@PermitAll
@ApplicationScoped
public class RestService {
	
	@EJB
	private DataService dataService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> loadAllProducts() throws DataServiceException {
		return dataService.loadAllProducts();
	}
	
	@POST
	@Path("/p")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("tracker")
	public List<Product> saveProduct(Product product)
			throws DataServiceException {
		return dataService.saveProduct(product);
	}
	
	@DELETE
	@Path("/p/{productName}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("tracker")
	public List<Product> removeProduct(@PathParam("productName") String productName)
			throws DataServiceException {
		return dataService.removeProduct(productName);
	}
	
	@DELETE
	@Path("/pv/{productName}:{version}")
	@RolesAllowed("tracker")
	public Response removeProductVersion(@PathParam("productName") String productName, 
			@PathParam("version") String version)
			throws DataServiceException {
		dataService.removeProductVersion(productName, version);
		return Response.ok().build();
	}
	
	@GET
	@Path("/pv/{productName}:{version}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductVersion getProductVersion(@PathParam("productName") String productName, 
			@PathParam("version") String version) throws DataServiceException {
		return dataService.getProductVersion(productName, version);
	}
	
	@GET
	@Path("/p/{productName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Product getProductByName(@PathParam("productName") String name) throws DataServiceException {
		return dataService.getProductByName(name);
	}
	
	@GET
	@Path("/p/vers/{productName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getVersions(@PathParam("productName") String productName)
			throws DataServiceException {
		return dataService.getVersions(productName);
	}
	
	@PUT
	@Path("/pv/{productName}/{versions}")
	@RolesAllowed("tracker")
	public Response addProductVersions(@PathParam("productName") String productName,
			@PathParam("versions") SetString versions) throws DataServiceException {
		Set<String> verSet = versions.asSet();
		dataService.addProductVersions(productName, verSet);
		return Response.ok().build();
	}
	
	@POST
	@Path("/pvp/{productName}:{version}/{parentProductName}:{parentVersion}")
	@RolesAllowed("tracker")
	public Response updatePVParent(@PathParam("productName") String productName, 
			@PathParam("version") String version, @PathParam("parentProductName") String parentProductName, 
			@PathParam("parentVersion") String parentVersion)
			throws DataServiceException {
		dataService.updateProductVersionParent(productName, version, parentProductName, parentVersion);
		return Response.ok().build();
	}
	
	@GET
	@Path("/a/{productName}:{version}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artifact> loadArtifacts(@PathParam("productName") String productName, 
			@PathParam("version") String version)
			throws DataServiceException {
		return dataService.loadArtifacts(productName, version);
	}
	
	@PUT
	@Path("/a/{productName}:{version}/{groupId}:{artifactId}:{artiVersion}")
	@RolesAllowed("tracker")
	public Response addArtifact(@PathParam("productName") String productName, @PathParam("version") String version,
			@PathParam("groupId") String groupId, @PathParam("artifactId") String artifactId, 
			@PathParam("artiVersion") String artiVersion) throws DataServiceException {
		dataService.addArtifact(productName, version, groupId, artifactId, artiVersion);
		return Response.ok().build();
	}
	
	@GET
	@Path("/a/{groupId}:{artifactId}:{artiVersion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Artifact getArtifact(@PathParam("groupId") String groupId, 
			@PathParam("artifactId") String artifactId, 
			@PathParam("artiVersion") String version) throws DataServiceException {
		return dataService.getArtifact(groupId, artifactId, version);
	}
	
	@GET
	@Path("/c/{groupId}:{artifactId}:{artiVersion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Component guessComponent(@PathParam("groupId") String groupId, 
			@PathParam("artifactId") String artifactId, 
			@PathParam("artiVersion") String artiVersion) throws DataServiceException {
		return dataService.guessComponent(groupId, artifactId, artiVersion);
	}
	
	@POST
	@Path("/ab/{groupId}:{artifactId}:{artiVersion}")
	@RolesAllowed("tracker")
	public Response updateArtifactBuildInfo(@PathParam("groupId") String groupId, 
			@PathParam("artifactId") String artifactId, 
			@PathParam("artiVersion") String artiVersion, @FormParam("buildInfo") String buildInfo) throws DataServiceException {
		dataService.updateArtifactBuildInfo(groupId, artifactId, artiVersion, buildInfo);
		return Response.ok().build();
	}
	
	@POST
	@Path("/ab/{groupId}:{artiVersion}")
	@RolesAllowed("tracker")
	public Response updateArtifactBuildInfo2(@PathParam("groupId") String groupId, 
			@PathParam("artiVersion") String artiVersion, @FormParam("buildInfo") String buildInfo) throws DataServiceException {
		dataService.updateArtifactBuildInfo(groupId, null, artiVersion, buildInfo);
		return Response.ok().build();
	}
	
	@POST
	@Path("/ac/{groupId}:{artifactId}:{artiVersion}/{compName}:{compVer}")
	@RolesAllowed("tracker")
	public Response updateArtifactComponent(@PathParam("groupId") String groupId, 
			@PathParam("artifactId") String artifactId, 
			@PathParam("artiVersion") String artiVersion, @PathParam("compName") String compName, 
			@PathParam("compVer") String compVer)
			throws DataServiceException {
		dataService.updateArtifactComponent(groupId, artifactId, artiVersion, compName, compVer);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/a/{productName}:{version}/{groupId}:{artifactId}:{artiVersion}")
	@RolesAllowed("tracker")
	public Response removeArtifacts(@PathParam("productName") String productName, @PathParam("version") String version,
			@PathParam("groupId") String groupId, @PathParam("artifactId") String artifactId, 
			@PathParam("artiVersion") String artiVersion)
			throws DataServiceException {
		dataService.removeArtifacts(productName, version, groupId, artifactId, artiVersion);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/ai/{productName}:{version}")
	@Encoded
	@RolesAllowed("tracker")
	public Response importArtifacts(@PathParam("productName") String productName, @PathParam("version") String version,
			@QueryParam("url") URL artifactListURL) throws DataServiceException {
		dataService.importArtifacts(productName, version, artifactListURL);
		return Response.ok().build();
	}
	
	
	@PUT
	@Path("/aiu/{productName}:{version}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("tracker")
	public Response importArtifactsWithUploadFile(@PathParam("productName") String productName, @PathParam("version") String version,
			MultipartFormDataInput multipPartInput) throws DataServiceException {
		
		try {
			InputStream input = getFileInputFromMultipartFormDataInput(multipPartInput);
			if (input == null) {
				return Response.status(Status.BAD_REQUEST).entity("No File upload").build();
			}
			// the getMatchRegexLines will close the InputStream at last
			List<String> artiStrs = DBDataService.getMatchRegexLines(input, DataService.ARTI_STR_REGEX);
			dataService.importArtifacts(productName, version, artiStrs);
		} catch (IOException e) {
			throw new DataServiceException("Can't read the uploaded file.", e);
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("/c/{name}:{version}")
	@Produces(MediaType.APPLICATION_JSON)
	public Component getComponent(@PathParam("name") String name, @PathParam("version") String version)
			throws DataServiceException {
		return dataService.getComponent(name, version);
	}
	
	@PUT
	@Path("/c")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("tracker")
	public Response saveComponent(Component comp) throws DataServiceException {
		dataService.saveComponent(comp);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/ci/{productName}:{version}")
	@RolesAllowed("tracker")
	public Response importNativeComponents(@PathParam("productName") String productName, @PathParam("version") String version,
			@QueryParam("url") URL composURL) throws DataServiceException {
		dataService.importNativeComponents(productName, version, composURL);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/ci")
	@RolesAllowed("tracker")
	public Response importComponents(@QueryParam("url") URL composURL) throws DataServiceException {
		dataService.importComponents(composURL);
		return Response.ok().build();
	}
	
	@PUT
	@Path("/ciu")
	@RolesAllowed("tracker")
	public Response importComponentsWithUploadFile(MultipartFormDataInput multipPartInput) throws DataServiceException {
		try {
			InputStream input = getFileInputFromMultipartFormDataInput(multipPartInput);
			if (input == null) {
				return Response.status(Status.BAD_REQUEST).entity("No File upload").build();
			}
			// the getMatchRegexLines will close the InputStream at last
			List<String> composStrs = DBDataService.getMatchRegexLines(input, DataService.COMP_STR_REGEX);
			dataService.importComponents(composStrs);
		} catch (IOException e) {
			throw new DataServiceException("Can't read the uploaded file.", e);
		}
		return Response.ok().build();
	}
	
	@PUT
	@Path("/ciu/{productName}:{version}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("tracker")
	public Response importNativeComponentsWithUploadFile(@PathParam("productName") String productName, @PathParam("version") String version,
			MultipartFormDataInput multipPartInput) throws DataServiceException {
		try {
			InputStream input = getFileInputFromMultipartFormDataInput(multipPartInput);
			if (input == null) {
				return Response.status(Status.BAD_REQUEST).entity("No File upload").build();
			}
			// the getMatchRegexLines will close the InputStream at last
			List<String> composStrs = DBDataService.getMatchRegexLines(input, DataService.COMP_STR_REGEX);
			dataService.importNativeComponents(productName, version, composStrs);
		} catch (IOException e) {
			throw new DataServiceException("Can't read the uploaded file.", e);
		}
		return Response.ok().build();
	}
	
	private InputStream getFileInputFromMultipartFormDataInput(MultipartFormDataInput multipPartInput) throws IOException {
		Map<String, List<InputPart>> formParts = multipPartInput.getFormDataMap();
		List<InputPart> filePart = formParts.get("file");
		InputPart firstFilePart = null;
		if (filePart != null && filePart.size() > 0) {
			// select the first one
			firstFilePart = filePart.get(0);
		}
		if (firstFilePart != null) {
			return firstFilePart.getBody(InputStream.class,null);
		}
		return null;
	}
	
	@POST
	@Path("/cg/{compName}:{compVer}/{groupId}")
	@RolesAllowed("tracker")
	public Response updateComponentGroupId(@PathParam("compName") String compName, 
			@PathParam("compVer") String compVer, @PathParam("groupId") String groupId)
			throws DataServiceException {
		Component comp = dataService.getComponent(compName, compVer);
		if (comp == null) {
			return Response.status(Status.NOT_FOUND).
					entity("No component: " + compName + ":" + compVer + " was found.").build();
		}
		comp.setGroupId(groupId); 
		dataService.saveComponent(comp);
		return Response.ok().build();
	}
	
	@POST
	@Path("/n/{type}-{id}")
	@RolesAllowed("tracker")
	public Response updateNote(@PathParam("id") Long id, @PathParam("type") String type, 
			@FormParam("note") String note)
			throws DataServiceException {
		dataService.updateNote(id, type, note);
		return Response.ok().build();
	}
	
}