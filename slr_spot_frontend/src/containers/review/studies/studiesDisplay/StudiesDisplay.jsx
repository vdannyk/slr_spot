import React from 'react';
import { Table } from "react-bootstrap";
import { AiOutlineClose } from "react-icons/ai";
import Helper from '../../../../components/helper/Helper';
import Check from 'react-bootstrap/FormCheck';
import FolderTree, { testData } from 'react-folder-tree';
import 'react-folder-tree/dist/style.css';
import './studiesDisplay.css';


const testImprots = [
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  }
]

const StudiesDisplay = () => {
  const onTreeStateChange = (state, event) => console.log(state, event);

  const listTestData = testImprots.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id + 1}</td>
        <td>{item.importedAt}</td>
        <td>{item.performedBy}</td>
        <td>{item.searchValue}</td>
        <td>{item.source}</td>
        <td>{item.studiesAdded}</td>
        <td>{item.duplicatesRemoved}</td>
        <td><AiOutlineClose /></td>
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__review-studiesDisplay'>
      <div className='slrspot__review-header'>
        <h1>Imported studies</h1>
      </div >
      <span>Show folders</span>
      <Check />
      <FolderTree
        data={ testData }
        onChange={ onTreeStateChange }
        showCheckbox={ false }
      />
      <Table>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
            </tr>
          </thead>
          { listTestData }
        </Table>
    </div>
  )
}

export default StudiesDisplay