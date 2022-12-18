package edu.odpr.odprapi.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static edu.odpr.odprapi.utils.Utils.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import edu.odpr.odprapi.model.Pattern;

@Service
public class OntologyAnalysisService {

    private void writeQueryReponses(int type) {
        String requestFileName;
        String responseFileName;
        switch (type) {
            case 0:
                requestFileName = "ClassNamesQueries.xml";
                responseFileName = "ClassNamesResponse.xml";
                break;
            case 1:
                requestFileName = "OPNamesQueries.xml";
                responseFileName = "OPNamesResponse.xml";
                break;
            case 2:
                requestFileName = "AxiomQueries.xml";
                responseFileName = "AxiomResponse.xml";
                break;
            default:
                requestFileName = "";
                responseFileName = "";
                break;
        }
        try {
            ProcessBuilder pb;
            Process p;
            pb = new ProcessBuilder("cmd", "/c", "Konclude.bat", "owllinkfile",
                    "-i", "requests\\" + requestFileName,
                    "-o", "responses\\" + responseFileName);
            pb.directory(new File(KONCLUDE_PATH));
            p = pb.start();
            final StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            int exitStatus = p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeXMLFile(String fileName, String xmlContent, String baseURI) {
        String tempPath = "src\\main\\java\\edu\\odpr\\odprapi\\utils\\Konclude\\requests\\";
        BufferedWriter writer = null;
        File file = null;

        if (baseURI != null) {
            xmlContent = xmlContent.replaceAll("replace", baseURI);
            xmlContent = xmlContent.replaceFirst("OntologyIRI IRI=\"\"",
                    "OntologyIRI IRI=\"requests/UserOntology.owl\"");
        }

        try {
            file = new File(tempPath + fileName);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(xmlContent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getResponses(int requestType) {
        String[] responses;
        String fileName;
        switch (requestType) {
            case 0:
                fileName = "ClassNamesResponse.xml";
                break;
            case 1:
                fileName = "OPNamesResponse.xml";
                break;
            case 2:
                fileName = "AxiomResponse.xml";
                break;
            default:
                fileName = "";
        }
        try {
            Document doc = getXMLDocument(KONCLUDE_PATH + "responses\\" + fileName);
            NodeList nList = doc.getChildNodes();
            nList = nList.item(1).getChildNodes();
            responses = new String[nList.getLength()];
            int i = 0;
            for (int temp = 3; temp < nList.getLength(); temp += 2) {
                if (temp > 3) {
                    Node nNode = nList.item(temp);
                    nNode.normalize();
                    if (requestType <= 1) {
                        responses[i] = nNode.getNodeName();
                    } else {
                        responses[i] = nNode.getAttributes().getNamedItem("result").getNodeValue();
                    }
                    
                    i++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            responses = null;
        }
        return responses;
    }

    private int findNameMatches(String[] responses, Dictionary<String, String> synonymsDict, int nameType,
            Dictionary<String, String> dict) {
        String requestsPath = KONCLUDE_PATH + "requests\\";
        int nameMatches = 0;
        if (nameType == 0) {
            requestsPath += "ClassNamesQueries.xml";
        } else {
            requestsPath += "OPNamesQueries.xml";
        }
        try {
            Document doc = getXMLDocument(requestsPath);
            Dictionary<String, String> originalNamesdict = new Hashtable<>();
            NodeList nList = doc.getChildNodes();
            nList = nList.item(0).getChildNodes();
            for (int temp = 2; temp < nList.getLength(); temp += 1) {
                Node nNode = nList.item(temp);
                String baseIRI = nNode.getFirstChild().getAttributes().getNamedItem("IRI").toString().split("#")[0];
                baseIRI = baseIRI.split("=\"")[1];
                String className = nNode.getFirstChild().getAttributes().getNamedItem("IRI").toString().split("#")[1];
                className = className.substring(0, className.length() - 1);
                String response = responses[temp - 2];
                if (response.equalsIgnoreCase("BooleanResponse")) {
                    dict.put(className, className);
                    synonymsDict.put(className, className);
                    nameMatches++;
                } else {
                    String syn = synonymsDict.get(className);
                    if (syn != null) {
                        ((Element) nNode.getFirstChild()).setAttribute("IRI", baseIRI + "#" + syn);
                        originalNamesdict.put(syn, className);
                    }
                }
            }
            writeXMLDocument(doc, requestsPath);
            this.writeQueryReponses(nameType);
            int synonymsMatches = this.processSynonymsResponses(this.getResponses(nameType), synonymsDict, originalNamesdict, dict, nameType);
            nameMatches += synonymsMatches;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return nameMatches;
    }

    private int processSynonymsResponses(String[] responses, Dictionary<String, String> synonymsDict,
            Dictionary<String, String> originalNames, Dictionary<String, String> dict, int nameType) {
        String requestsPath = KONCLUDE_PATH + "requests\\";
        int nameMatches = 0;
        if (nameType == 0) {
            requestsPath += "ClassNamesQueries.xml";
        } else {
            requestsPath += "OPNamesQueries.xml";
        }
        try {
            Document doc = getXMLDocument(requestsPath);
            NodeList nList = doc.getChildNodes();
            nList = nList.item(0).getChildNodes();
            for (int temp = 2; temp < nList.getLength(); temp += 1) {
                Node nNode = nList.item(temp);
                String baseIRI = nNode.getFirstChild().getAttributes().getNamedItem("IRI").toString().split("#")[0];
                baseIRI = baseIRI.split("=\"")[1];
                String className = nNode.getFirstChild().getAttributes().getNamedItem("IRI").toString().split("#")[1];
                className = className.substring(0, className.length() - 1);
                String response = responses[temp - 2];
                if (response.equalsIgnoreCase("BooleanResponse")) {
                    if (dict.get(className) == null) {
                        dict.put(originalNames.get(className), className);
                        nameMatches++;
                    }
                    
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return nameMatches;
    }

    private int findAxiomMatches(Dictionary<String, String> classNamesdict, Dictionary<String, String> opNamesdict) {
        int axiomCount = 0;
        try {
            Document doc = getXMLDocument(KONCLUDE_PATH + "requests\\AxiomQueries.xml");
            String xmlString = convertDocumentToString(doc);
            Enumeration<String> e;
            String key;
            e = classNamesdict.keys();
            while (e.hasMoreElements()) {
                key = e.nextElement();
                xmlString = xmlString.replaceAll("#" + key + "\"", "#" + classNamesdict.get(key) + "\"");
            }

            e = opNamesdict.keys();
            while (e.hasMoreElements()) {
                key = e.nextElement();
                xmlString = xmlString.replaceAll("#" + key + "\"", "#" + opNamesdict.get(key) + "\"");
            }

            this.writeXMLFile("AxiomQueries.xml", xmlString, null);
            this.writeQueryReponses(2);

            String[] responses = this.getResponses(2);
            for (int i=0; i<responses.length; i++) {
                if (responses[i] == null) {
                    break;
                }
                if (responses[i].equalsIgnoreCase("true")) {
                    axiomCount++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return axiomCount;
    }

    public Map<String, String> postProcessResults(Pattern p, int classNameMatches, int opNameMatches, int axiomMatches) {
        float totalClassNames = p.getCantClasses();
        float totalOPNames = p.getCantOPs();
        float totalAxiomas = p.getCantAxioms();
        String patterName = p.getName();
        Map<String, String> results = new HashMap<String, String>();

        if (classNameMatches >= totalClassNames / 2) {
            // TODO cambiar 2da evaluacion en el informa, solo tiene que pasar el 30% en OPs y Axiomas
            String type = "partial";
            //float classesAvg = Math.round(((classNameMatches / totalClassNames) * 10));
            float opsAvg = Math.round((opNameMatches / totalOPNames) * 10);
            float axiomsAvg = Math.round((axiomMatches / totalAxiomas) * 10);

            //System.out.println(classesAvg+" "+opsAvg+" "+axiomsAvg);
            if (opsAvg >= 3 && axiomMatches >=3) {
                type = "total";
            }

            results.put("pattern_name", patterName);
            results.put("recommendation_type", type);
            results.put("class_names_matches", String.valueOf(classNameMatches));
            results.put("objectproperty_names_matches", String.valueOf(opNameMatches));
            results.put("axioms_matches", String.valueOf(axiomMatches));
        } else {
            // todo not suggested
        }
        return results;
    }

    public LinkedList<Map<String, String>> analyseOntology(List<Pattern> patternList, InputStream ontologyIS, String ontologyStr) {
        LinkedList<Map<String, String>> recommendationList = new LinkedList<Map<String, String>>();
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology o = manager.loadOntologyFromOntologyDocument(ontologyIS);
            String baseURI = o.getOntologyID().getOntologyIRI().get().getIRIString();

            writeXMLFile("UserOntology.owl", ontologyStr, null);
            
            for (Pattern p : patternList) {
                writeXMLFile("ClassNamesQueries.xml", p.getClassNamesQueries(), baseURI);
                writeXMLFile("OPNamesQueries.xml", p.getOpNamesQueries(), baseURI);
                writeXMLFile("AxiomQueries.xml", p.getAxiomsQueries(), baseURI);

                this.writeQueryReponses(0);
                this.writeQueryReponses(1);
                Object obj = new JSONParser().parse(p.getSynonyms());
                JSONObject jo = (JSONObject) obj;

                Dictionary<String, String> synonymsDict = getDictFromJO(jo);
                Dictionary<String, String> classNamesdict = new Hashtable<>();
                Dictionary<String, String> opNamesdict = new Hashtable<>();
                int classNameMatches = this.findNameMatches(this.getResponses(0), synonymsDict, 0, classNamesdict);
                int opNameMatches = this.findNameMatches(this.getResponses(1), synonymsDict, 1, opNamesdict);
                int axiomMatches = this.findAxiomMatches(classNamesdict, opNamesdict);
                int totalClassNames = p.getCantClasses();
                int totalOPNames = p.getCantOPs();
                int totalAxiomas = p.getCantAxioms();
                System.out.println("Cantidad de matcheos de clases: "+classNameMatches);
                System.out.println("Cantidad de matcheos de ops: "+opNameMatches);
                System.out.println("Cantidad de matcheos de axiomas: "+axiomMatches);
                System.out.println("Cantidad total de clases: "+totalClassNames);
                System.out.println("Cantidad total de ops: "+totalOPNames);
                System.out.println("Cantidad total de axiomas: "+totalAxiomas);

                Map<String, String> patternResults = this.postProcessResults(p, classNameMatches, opNameMatches, axiomMatches);
                recommendationList.add(patternResults);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendationList;
    }

}
