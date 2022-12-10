import React from 'react';
import { AiOutlineClose } from "react-icons/ai";
import { Table } from "react-bootstrap";

import './studiesView.css';

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

const StudiesView = () => {

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
    <div>
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

export default StudiesView