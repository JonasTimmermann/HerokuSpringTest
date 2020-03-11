/** 

package de.emp2020.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NamingService {
	
	public NamingService(NamingRepo repo){
		this.nameRepo = repo;
	}

	@Autowired
	NamingRepo nameRepo;


	public List<NameableNode> getAllMappings(){

		//List<NameableNode> listMapping = nameRepo.findAll();
		List<NameableNode> listMapping = new ArrayList<>();
		nameRepo.findAll().forEach(listMapping::add);
		//listMapping = (List<NameableNode>) nameRepo.findAll();
		//questionRepo.findAll().forEach(frageList::add);
		
		return listMapping;
	}



	public NameableNode getNameableNodeById(Long id) {
		return nameRepo.findById(id).orElse(null);
	}

	
	public NameableNode editMapping(NameableNode mapping){
		
		NameableNode nodeParam = mapping;
		Long id = nodeParam.getId();
		NameableNode node = getNameableNodeById(id);

		if(node == null || id != nodeParam.getId()){
			return null;
		}

		if(node.getTargetName() == nodeParam.getTargetName() && node.getMapsTo() != nodeParam.getMapsTo()){
			
			node.setMapsTo(nodeParam.getMapsTo());
			nameRepo.save(node);
		}
		
		return node;
	}


	public NameableNode saveMapping(NameableNode mapping){
		
		NameableNode nodeParam = mapping;
		Long idNp = nodeParam.getId();
		List<NameableNode> listNode = getAllMappings();

		for(int i = 0; i < listNode.size(); i++) {
			if(idNp == listNode.get(i).getId()){

				return null;
			}
		}

		nameRepo.save(nodeParam);
	
		return nodeParam;
	}
	
}


**/