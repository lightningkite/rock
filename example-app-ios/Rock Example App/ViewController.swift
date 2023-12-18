//
//  ViewController.swift
//  Rock Example App
//
//  Created by Joseph Ivie on 12/14/23.
//

import UIKit
import shared

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let writer = ViewWriter(parent: self.view, startDepth: 0)
        print("Beginning write...")
        writer.iosTest()
        print("Write complete.")
        self.view.subviews.forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            $0.topAnchor.constraint(equalTo: self.view.topAnchor).isActive = true
            $0.bottomAnchor.constraint(equalTo: self.view.bottomAnchor).isActive = true
            $0.leftAnchor.constraint(equalTo: self.view.leftAnchor).isActive = true
            $0.rightAnchor.constraint(equalTo: self.view.rightAnchor).isActive = true
        }
    }


}

